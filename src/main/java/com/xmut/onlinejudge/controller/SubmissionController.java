package com.xmut.onlinejudge.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Submission;
import com.xmut.onlinejudge.service.ProblemService;
import com.xmut.onlinejudge.service.SubmissionService;
import com.xmut.onlinejudge.utils.DateUtil;
import com.xmut.onlinejudge.utils.JudgeUtil;
import com.xmut.onlinejudge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;

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

    /**
     * 添加。
     *
     * @param submission
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Submission submission) {
        return submissionService.save(submission);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return submissionService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param submission
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Submission submission) {
        return submissionService.updateById(submission);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<Submission> list() {
        return submissionService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public Submission getInfo(@PathVariable Serializable id) {
        return submissionService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<Submission> page(Page<Submission> page) {
        return submissionService.page(page);
    }


    @PostMapping("")
    public Result<Submission> submit(@RequestBody Submission submission) throws JsonProcessingException {
        Result<Submission> result = new Result<>();
        submission.setCreateTime(DateUtil.getCurrTime());
        submission.setUserId(JwtUtil.getUserId(request.getHeader("token")));
        submissionService.save(submission);
        //异步判题
        judgeUtil.judge(submission, problemService.getById(submission.getProblemId()));

        result.success(submission, "提交成功");
        return result;
    }

    @GetMapping("")
    public Result<Submission> submit(String id) {
        Result<Submission> result = new Result<>();
        Submission submission = submissionService.getById(id);

        result.success(submission, "提交成功");
        return result;
    }

}
