package com.xmut.onlinejudge.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.entity.Submission;
import com.xmut.onlinejudge.service.OptionsSysoptionsService;
import com.xmut.onlinejudge.service.SubmissionService;
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
    @Autowired
    private SubmissionService submissionService;

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

    public static String request(String url, String data) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Judge-Server-Token", sha256(token));
        //创建请求体
        HttpEntity<String> entity = new HttpEntity<>(data, headers);
        //发送请求
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        JSONObject result = JSON.parseObject(responseEntity.getBody());
        System.out.println(result);
        System.out.println(responseEntity.getBody());
        return result.getString("data");
    }

    //    public static JudgeServer ping(String url) {
//        String response=request(url,null);
//        System.out.println(response);
//        JSONObject result = JSON.parseObject(response);
//        JSONObject data = JSON.parseObject(result.getString("data"));
//        JudgeServer judgeServer = new JudgeServer(data);
//        judgeServer.setServiceUrl(url);
//        System.out.println(judgeServer);
//        return judgeServer;
//    }

    public static void main(String[] argc) throws JsonProcessingException {
        JudgeUtil judgeUtil = new JudgeUtil();
        Submission submission = new Submission();
//        judgeUtil.handleJudgeTask(submission);
//        judgeService.ping ("http://192.168.214.134:808/ping");
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
    public void judge(Submission submission, Problem problem) throws JsonProcessingException {
        // 处理判题任务的逻辑，包括编译、运行等步骤
        // 通过异步队列或者消息队列发送判题任务
        // 可以在这里调用判题服务的接口或者方法
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyPort", "8888");

        String judgeUrl = "http://192.168.214.134:8080/judge";
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

}
