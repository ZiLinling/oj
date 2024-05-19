package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Submission;
import com.xmut.onlinejudge.service.ProblemService;
import com.xmut.onlinejudge.service.SubmissionService;
import com.xmut.onlinejudge.utils.DateUtil;
import com.xmut.onlinejudge.utils.JudgeUtil;
import com.xmut.onlinejudge.utils.JwtUtil;
import com.xmut.onlinejudge.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/submission")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private JudgeUtil judgeUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RedisUtil redisUtil;


    @GetMapping("list")
    public Result<Page<Submission>> page(Integer limit, Integer page, Integer contestId, Boolean myself, String username, @RequestParam(value = "result", required = false) Integer resultStatus, Integer problemId) {
        Result<Page<Submission>> result = new Result<>();
        Integer userId = null;
        String token = request.getHeader("token");
        if (myself && JwtUtil.verifyToken(token)) {
            userId = JwtUtil.getUserId(token);
        }
        Page<Submission> submissionPage = submissionService.page(page, limit, contestId, username, resultStatus, userId, problemId);
        result.success(submissionPage, "查询成功");
        return result;
    }


    @PostMapping("")
    public Result<Submission> submit(@RequestBody Submission submission) throws JsonProcessingException {
        Result<Submission> result = new Result<>();
        submission.setCreateTime(DateUtil.getCurrTime());
        submission.setUserId(JwtUtil.getUserId(request.getHeader("token")));
        submission.setUsername(JwtUtil.getUsername(request.getHeader("token")));
        submission.setIp(request.getRemoteAddr());
        submissionService.save(submission);
        //将提交放入任务队列
        JSONObject task = new JSONObject();
        task.put("submission", submission);
        task.put("problem", problemService.getById(submission.getProblemId()));
        redisUtil.lPush("task_queue", task);
        //异步判题
        judgeUtil.judge();
        result.success(submission, "提交成功");
        return result;
    }

    @GetMapping("")
    public Result<Submission> getInfo(String id) {
        Result<Submission> result = new Result<>();
        Submission submission = submissionService.getById(id);
        result.success(submission, "提交成功");
        return result;
    }

    @GetMapping("exist")
    public Result<Boolean> judgeExist(Integer problemId) {
        Result<Boolean> result = new Result<>();
        String token = request.getHeader("token");
        Boolean isExist = false;
        if (JwtUtil.verifyToken(token)) {
            isExist = submissionService.isExist(problemId, JwtUtil.getUserId(token));
        }
        result.success(isExist, "查询成功");
        return result;
    }

    @PutMapping("/update")
    public Result<Submission> update(@RequestBody Submission submission) {
        Result<Submission> result = new Result<>();
        submissionService.updateById(submission);
        result.success(submission, "更新成功");
        return result;
    }

}
