package com.xmut.onlinejudge.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmut.onlinejudge.entity.*;
import com.xmut.onlinejudge.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component

public class JudgeUtil {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private OptionsSysoptionsService optionsSysoptionsService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ContestService contestService;


    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private AcmContestRankService acmContestRankService;

    @Autowired
    private OiContestRankService oiContestRankService;

    public static final int COMPILE_ERROR = -2;

    private static String token = "TOKEN";
    private String serverBaseUrl;
    public static final int WRONG_ANSWER = -1;
    public static final int ACCEPTED = 0;
    public static final int CPU_TIME_LIMIT_EXCEEDED = 1;
    public static final int REAL_TIME_LIMIT_EXCEEDED = 2;
    public static final int MEMORY_LIMIT_EXCEEDED = 3;
    public static final int RUNTIME_ERROR = 4;
    public static final int SYSTEM_ERROR = 5;
    public static final int PENDING = 6;
    public static final int JUDGING = 7;
    public static final int PARTIALLY_ACCEPTED = 8;


    JudgeUtil() {
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
    }


    private static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getLanguageConfig(String language) {
        JSONArray data;
        if (redisUtil.hasKey("languages")) {
            data = (JSONArray) redisUtil.get("languages");
        } else {
            data = (JSONArray) optionsSysoptionsService.getValue("languages");
            redisUtil.set("languages", data, 60 * 60);
        }
        JSONObject languageConfig = null;
        for (Object item : data) {
            languageConfig = (JSONObject) item;
            if (languageConfig.getString("name").equals(language)) {
                break;
            }
        }
        JSONObject config = languageConfig.getJSONObject("config");
        return config;
    }

    @Async
    public void judge() throws JsonProcessingException {
        // 处理判题任务的逻辑，包括编译、运行等步骤
        // 通过异步队列或者消息队列发送判题任务
        // 可以在这里调用判题服务的接口或者方法
        if (redisUtil.llen("task_queue") == 0) {
            return;
        }
        JSONObject task = (JSONObject) redisUtil.rPop("task_queue");
        Submission submission = (Submission) task.get("submission");
        Problem problem = (Problem) task.get("problem");

        String judgeUrl = "http://localhost:7777/judge";
        //向判题服务器发送post请求
        RestTemplate restTemplate = new RestTemplate();

        //创建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Judge-Server-Token", sha256(token));

        JSONObject taskInfo = new JSONObject();
        taskInfo.put("language_config", getLanguageConfig(submission.getLanguage()));
        taskInfo.put("src", submission.getCode());
        taskInfo.put("max_cpu_time", problem.getTimeLimit());
        taskInfo.put("max_memory", problem.getMemoryLimit() * 1024 * 1024);
        taskInfo.put("test_case_id", problem.getTestCaseId());
        taskInfo.put("spj_version", problem.getSpjVersion());
        taskInfo.put("spj_config", null);
        taskInfo.put("spj_compile_config", null);
        taskInfo.put("spj_src", problem.getSpjCode());
        taskInfo.put("output", true);
        //创建请求体
        HttpEntity<String> entity = new HttpEntity<>(taskInfo.toString(), headers);
        //发送请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(judgeUrl, entity, String.class);
        String data = responseEntity.getBody();
        JSONObject result = JSON.parseObject(data);
        if (result.get("err") != null) {
            JSONObject staticInfo = new JSONObject();
            staticInfo.put("err_info", result.get("data"));
            staticInfo.put("score", 0);
            submission.setResult(COMPILE_ERROR);
            submission.setStatisticInfo(staticInfo);
        } else {
            submission.setInfo(result);
            JSONArray testData = result.getObject("data", JSONArray.class);
            computeStatisticInfo(submission, testData, problem);
            JSONArray errorTestCase = testData.stream()
                    .filter(caseObj -> ((JSONObject) caseObj).getIntValue("result") != 0)
                    .collect(JSONArray::new, JSONArray::add, JSONArray::addAll);
            //ACM模式下,多个测试点全部正确则AC，否则取第一个错误的测试点的状态
            //OI模式下, 若多个测试点全部正确则AC， 若全部错误则取第一个错误测试点状态，否则为部分正确
            if (errorTestCase.isEmpty()) {
                submission.setResult(ACCEPTED);
            } else if (problem.getRuleType().equals("ACM") || errorTestCase.size() == testData.size()) {
                submission.setResult(errorTestCase.getJSONObject(0).getIntValue("result"));
            } else {
                submission.setResult(PARTIALLY_ACCEPTED);
            }
        }
        submissionService.saveOrUpdate(submission);
        update_problem_status(submission, problem);
        if (submission.getContestId() != null) {
            Contest contest = contestService.getById(submission.getContestId());
            update_contest_rank(submission, problem, contest);
        } else {
            update_userprofile(submission, problem);
        }
        judge();
    }

    private void computeStatisticInfo(Submission submission, JSONArray testData, Problem problem) {
        // 用时和内存占用保存为多个测试点中最长的那个
        int timeCost = testData.stream().mapToInt(o -> ((JSONObject) o).getIntValue("cpu_time")).max().orElse(0);
        int memoryCost = testData.stream().mapToInt(o -> ((JSONObject) o).getIntValue("memory")).max().orElse(0);
        JSONObject staticInfo = new JSONObject();
        staticInfo.put("time_cost", timeCost);
        staticInfo.put("memory_cost", memoryCost);

        // sum up the score in OI mode
        if (problem.getRuleType().equals("OI")) {
            int score = 0;
            for (int i = 0; i < testData.size(); i++) {
                JSONObject testCase = testData.getJSONObject(i);
                if (testCase.getIntValue("result") == ACCEPTED) {
                    int testCaseScore = problem.getTestCaseScore().getJSONObject(i).getIntValue("score");
                    testCase.put("score", testCaseScore);
                    score += testCaseScore;
                } else {
                    testCase.put("score", 0);
                }
                testData.set(i, testCase);
            }
            staticInfo.put("score", score);
        }
        submission.setStatisticInfo(staticInfo);
    }

    private void update_problem_status(Submission submission, Problem problem) {
        int result = submission.getResult();
        problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
        if (submission.getResult() == ACCEPTED) {
            problem.setAcceptedNumber(problem.getAcceptedNumber() + 1);
        }
        JSONObject problemInfo = problem.getStatisticInfo();
        problemInfo.put(String.valueOf(result), problemInfo.getIntValue(String.valueOf(result)) + 1);
        problem.setStatisticInfo(problemInfo);
        problemService.updateById(problem);
    }

    private void update_userprofile(Submission submission, Problem problem) {
        // update_userprofile
        UserProfile userProfile = userProfileService.getByUserId(submission.getUserId());
        userProfile.setSubmissionNumber(userProfile.getSubmissionNumber() + 1);
        if (problem.getRuleType().equals("ACM")) {
            JSONObject acmProblemsStatus = userProfile.getAcmProblemsStatus();
            if (!acmProblemsStatus.containsKey(String.valueOf(problem.getId()))) {
                acmProblemsStatus.put(String.valueOf(problem.getId()), new JSONObject());
                acmProblemsStatus.getJSONObject(String.valueOf(problem.getId())).put("status", submission.getResult());
                acmProblemsStatus.getJSONObject(String.valueOf(problem.getId())).put("displayId", problem.getDisplayId());
                if (submission.getResult() == ACCEPTED) {
                    userProfile.setAcceptedNumber(userProfile.getAcceptedNumber() + 1);
                }
            } else {
                if (acmProblemsStatus.getJSONObject(String.valueOf(problem.getId())).getIntValue("status") != ACCEPTED) {
                    acmProblemsStatus.getJSONObject(String.valueOf(problem.getId())).put("status", submission.getResult());
                    if (submission.getResult() == ACCEPTED) {
                        userProfile.setAcceptedNumber(userProfile.getAcceptedNumber() + 1);
                    }
                }
            }
            userProfile.setAcmProblemsStatus(acmProblemsStatus);
        } else {
            JSONObject oiProblemsStatus = userProfile.getOiProblemsStatus();
            int score = submission.getStatisticInfo().getIntValue("score");
            if (!oiProblemsStatus.containsKey(String.valueOf(problem.getId()))) {
                userProfile.addScore(score);
                oiProblemsStatus.put(String.valueOf(problem.getId()), new JSONObject());
                oiProblemsStatus.getJSONObject(String.valueOf(problem.getId())).put("status", submission.getResult());
                oiProblemsStatus.getJSONObject(String.valueOf(problem.getId())).put("displayId", problem.getDisplayId());
                oiProblemsStatus.getJSONObject(String.valueOf(problem.getId())).put("score", score);
                if (submission.getResult() == ACCEPTED) {
                    userProfile.setAcceptedNumber(userProfile.getAcceptedNumber() + 1);
                }
            } else {
                if (oiProblemsStatus.getJSONObject(String.valueOf(problem.getId())).getIntValue("status") != ACCEPTED) {
                    userProfile.addScore(score, oiProblemsStatus.getJSONObject(String.valueOf(problem.getId())).getIntValue("score"));
                    oiProblemsStatus.getJSONObject(String.valueOf(problem.getId())).put("score", score);
                    oiProblemsStatus.getJSONObject(String.valueOf(problem.getId())).put("status", submission.getResult());
                    if (submission.getResult() == ACCEPTED) {
                        userProfile.setAcceptedNumber(userProfile.getAcceptedNumber() + 1);
                    }
                }
            }
            userProfile.setOiProblemsStatus(oiProblemsStatus);
        }
        userProfileService.updateById(userProfile);
    }

    //        if self.contest.rule_type == ContestRuleType.OI or self.contest.real_time_rank:
//            cache.delete(f"{CacheKey.contest_rank_cache}:{self.contest.id}")
    private void update_contest_rank(Submission submission, Problem problem, Contest contest) {
        if (contest.getRuleType().equals("OI") || contest.getRealTimeRank()) {
            redisUtil.del("contest_rank_cache:" + contest.getId());
        }
        if (contest.getRuleType().equals("ACM")) {
            AcmContestRank acmContestRank = acmContestRankService.getByUserIdAndContestId(submission.getUserId(), contest.getId());
            if (acmContestRank == null) {
                acmContestRank = new AcmContestRank(contest.getId(), submission.getUserId());
                acmContestRankService.save(acmContestRank);
            }
            update_acm_contest_rank(submission, problem, contest, acmContestRank);
        } else {
            OiContestRank oiContestRank = oiContestRankService.getByUserIdAndContestId(submission.getUserId(), contest.getId());
            if (oiContestRank == null) {
                oiContestRank = new OiContestRank(contest.getId(), submission.getUserId());
                oiContestRankService.save(oiContestRank);
            }
            update_oi_contest_rank(submission, problem, contest, oiContestRank);
        }

    }

    private void update_acm_contest_rank(Submission submission, Problem problem, Contest contest, AcmContestRank acmContestRank) {
        JSONObject submissionInfo = acmContestRank.getSubmissionInfo();
        JSONObject info = submissionInfo.getJSONObject(String.valueOf(submission.getProblemId()));
        if (info != null) {
            if (info.getBoolean("is_ac")) {
                return;
            }
            acmContestRank.setSubmissionNumber(acmContestRank.getSubmissionNumber() + 1);
            if (submission.getResult() == ACCEPTED) {
                acmContestRank.setAcceptedNumber(acmContestRank.getAcceptedNumber() + 1);
                info.put("is_ac", true);
                info.put("ac_time", DateUtil.getDiffSeconds(contest.getStartTime(), submission.getCreateTime()));
                acmContestRank.setTotalTime(info.getIntValue("ac_time") + info.getIntValue("error_number") * 20 * 60);
                if (problem.getAcceptedNumber() == 1) {
                    info.put("is_first_ac", true);
                }
            } else if (submission.getResult() != COMPILE_ERROR) {
                info.put("error_number", info.getIntValue("error_number") + 1);
            }
        } else {
            acmContestRank.setSubmissionNumber(acmContestRank.getSubmissionNumber() + 1);
            info = new JSONObject();
            info.put("is_ac", false);
            info.put("ac_time", 0);
            info.put("error_number", 0);
            info.put("is_first_ac", false);
            if (submission.getResult() == ACCEPTED) {
                acmContestRank.setAcceptedNumber(acmContestRank.getAcceptedNumber() + 1);
                info.put("is_ac", true);
                info.put("ac_time", DateUtil.getDiffSeconds(contest.getStartTime(), submission.getCreateTime()));
                acmContestRank.setTotalTime(info.getIntValue("ac_time"));
                if (problem.getAcceptedNumber() == 1) {
                    info.put("is_first_ac", true);
                }
            } else if (submission.getResult() != COMPILE_ERROR) {
                info.put("error_number", 1);
            }
        }
        submissionInfo.put(String.valueOf(submission.getProblemId()), info);
        acmContestRank.setSubmissionInfo(submissionInfo);
        acmContestRankService.saveOrUpdate(acmContestRank);
    }

    private void update_oi_contest_rank(Submission submission, Problem problem, Contest contest, OiContestRank oiContestRank) {
        String problemId = String.valueOf(submission.getProblemId());
        int currentScore = submission.getStatisticInfo().getIntValue("score");
        JSONObject info = oiContestRank.getSubmissionInfo();
        if (currentScore < info.getIntValue(problemId)) {
            return;
        }
        System.out.println(oiContestRank.getSubmissionInfo());
        Integer lastScore = oiContestRank.getSubmissionInfo().getInteger(problemId);
        if (lastScore != null) {
            oiContestRank.setTotalScore(oiContestRank.getTotalScore() - lastScore + currentScore);
        } else {
            oiContestRank.setTotalScore(oiContestRank.getTotalScore() + currentScore);
        }
        oiContestRank.setSubmissionNumber(oiContestRank.getSubmissionNumber() + 1);
        oiContestRank.getSubmissionInfo().put(problemId, currentScore);
        oiContestRankService.saveOrUpdate(oiContestRank);
    }


}
