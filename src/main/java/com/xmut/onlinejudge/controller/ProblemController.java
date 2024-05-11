package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.VO.ProblemWithTags;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.entity.ProblemTag;
import com.xmut.onlinejudge.entity.ProblemTags;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.service.ProblemService;
import com.xmut.onlinejudge.service.ProblemTagService;
import com.xmut.onlinejudge.service.ProblemTagsService;
import com.xmut.onlinejudge.service.UserProfileService;
import com.xmut.onlinejudge.utils.FileUtil;
import com.xmut.onlinejudge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ProblemTagService tagService;

    @Autowired
    private ProblemTagsService tagsService;

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private HttpServletRequest request;

    @Value("${files.upload.testCase.path}")
    private String TEST_CASES_DIR;


    @GetMapping("")
    public Result<Problem> getByDisplayId(String displayId) {
        Result<Problem> result = new Result<>();
        ProblemWithTags problem = problemService.getByDisplayId(displayId, false);
        if (JwtUtil.verifyToken(request.getHeader("token"))) {
            List<ProblemWithTags> problems = new ArrayList<>();
            problems.add(problem);
            problems = addProblemStatus(problems);
            problem = problems.get(0);
        }
        if (problem != null) {
            result.success(problem, "查询成功");
        } else {
            result.error("该问题不存在");
        }
        return result;
    }

    @GetMapping("/admin")
    public Result<ProblemWithTags> getById(Integer id) {
        Result<ProblemWithTags> result = new Result<>();
        ProblemWithTags problem = problemService.getById(id);
        if (problem != null) {
            result.success(problem, "查询成功");
        } else {
            result.error("该问题不存在");
        }
        return result;
    }

    @PutMapping("/admin")
    public Result<ProblemWithTags> updateById(@RequestBody ProblemWithTags problem) {
        Result<ProblemWithTags> result = new Result<>();
        ProblemWithTags getter = problemService.getByDisplayId(problem.getDisplayId(), true);
        if (Objects.equals(getter.getId(), problem.getId())) {
            List<String> tags = problem.getTags();
            problemService.updateById(problem);
            updateTags(tags, problem.getId());
            result.success(null, "更新成功");
        } else {
            result.error("显示ID重复,请修改");
        }
        return result;
    }

    @PostMapping("/admin")
    public Result<ProblemWithTags> save(@RequestBody ProblemWithTags problem, @RequestHeader("token") String token) {
        Result<ProblemWithTags> result = new Result<>();
        problem.setCreatedById(JwtUtil.getUserId(token));
        Integer totalScore = 0;
        JSONArray testCaseScore = problem.getTestCaseScore();
        for (int i = 0; i < testCaseScore.size(); i++) {
            totalScore += testCaseScore.getJSONObject(i).getInteger("score");
        }
        problem.setTotalScore(totalScore);
        ProblemWithTags getter = problemService.getByDisplayId(problem.getDisplayId(), true);
        if (getter != null) {
            if (Objects.equals(getter.getId(), problem.getId())) {
                result.error("显示ID重复,请修改");
            }
        } else {
            System.out.println(problem.getId());
            List<String> tags = problem.getTags();
            problemService.save(problem);
            updateTags(tags, problem.getId());
            result.success(null, "新增成功");
        }
        return result;
    }


    public void updateTags(List<String> tags, Integer problemId) {
        //删除tags表中problemId对应的数据
        tagsService.deleteByProblemId(problemId);
        //如果tag表中存在tag,则不新增
        //根据tags表中的tag,在tag表中查找tagId,并插入tags表中
        for (String tag : tags) {
            ProblemTag problemTag = tagService.getByName(tag);
            if (problemTag == null) {
                problemTag = new ProblemTag();
                problemTag.setName(tag);
                tagService.save(problemTag);
            }
            tagsService.save(new ProblemTags(null, problemId, problemTag.getId()));
        }
    }

    public List<ProblemWithTags> addProblemStatus(List<ProblemWithTags> problems) {
        UserProfile profile = userProfileService.getByUserId(JwtUtil.getUserId(request.getHeader("token")));
        JSONObject oiStatus = profile.getOiProblemsStatus();
        JSONObject acmStatus = profile.getAcmProblemsStatus();
        for (int i = 0; i < problems.size(); i++) {
            ProblemWithTags problem = problems.get(i);
            String ruleType = problem.getRuleType();
            String problemId = problem.getId().toString();
            JSONObject problemInfo;
            if (oiStatus.containsKey(problemId) && ruleType.equals("OI")) {
                problemInfo = JSON.parseObject(oiStatus.getString(problemId));
                Integer status = problemInfo.getInteger("status");
                problem.setMyStatus(status);
                problems.set(i, problem);
            } else if (acmStatus.containsKey(problemId) && ruleType.equals("ACM")) {
                problemInfo = JSON.parseObject(acmStatus.getString(problemId));
                Integer status = problemInfo.getInteger("status");
                problem.setMyStatus(status);
                problems.set(i, problem);
            }
        }
        return problems;
    }


    @GetMapping("list")
    public Result<Page<ProblemWithTags>> pageForUser(Integer limit, Integer page, String keyword, String difficulty, String tag) {
        Result<Page<ProblemWithTags>> result = new Result<>();
        Page<ProblemWithTags> problemPage = problemService.page(page, limit, keyword, difficulty, tag, false);
        List<ProblemWithTags> records = problemPage.getRecords();
        if (JwtUtil.verifyToken(request.getHeader("token"))) {
            records = addProblemStatus(records);
            problemPage.setRecords(records);
        }
        result.success(problemPage, "查询成功");
        return result;
    }

    @GetMapping("admin/list")
    public Result<Page<ProblemWithTags>> pageForAdmin(Integer limit, Integer page, String keyword, String difficulty, String tag) {
        Result<Page<ProblemWithTags>> result = new Result<>();
        Page<ProblemWithTags> problemPage = problemService.page(page, limit, keyword, difficulty, tag, true);
        List<ProblemWithTags> records = problemPage.getRecords();
        if (JwtUtil.verifyToken(request.getHeader("token"))) {
            records = addProblemStatus(records);
            problemPage.setRecords(records);
        }
        result.success(problemPage, "查询成功");
        return result;
    }


    @PostMapping("test_case")
    public Result<JSONObject> uploadTestCase(@RequestBody MultipartFile file, Boolean spj) {
        Result<JSONObject> result = new Result<>();
        try {
            JSONObject info = fileUtil.processZip(file, spj, TEST_CASES_DIR);
            result.success(info, "上传成功");
        } catch (Exception e) {
            result.error("上传失败");
        }
        return result;
    }

    @GetMapping("test_case")
    public Object downloadTestCase(Integer problemId) throws IOException {
        Problem problem = problemService.getById(problemId);
        String testCaseId = problem.getTestCaseId();
        Path zipFilePath = fileUtil.createTestCaseZip(testCaseId, TEST_CASES_DIR);
        byte[] rawData = Files.readAllBytes(zipFilePath);
        Files.deleteIfExists(zipFilePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "problem_" + problemId + "_test_cases.zip");
        // 可选：下载后删除文件
        Files.deleteIfExists(zipFilePath);
        return ResponseEntity.ok()
                .headers(headers)
                .body(rawData);
    }

}
