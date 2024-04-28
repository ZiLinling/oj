package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.service.ProblemService;
import com.xmut.onlinejudge.service.UserProfileService;
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
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加。
     *
     * @param problem
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Problem problem) {
        return problemService.save(problem);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return problemService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param problem
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Problem problem) {
        return problemService.updateById(problem);
    }


    @GetMapping("")
    public Result<JSONObject> getInfo(String displayId) {
        Result<JSONObject> result = new Result<>();
        JSONObject problem = (JSONObject) JSONObject.toJSON(problemService.getByDisplayId(displayId));
        if (JwtUtil.verifyToken(request.getHeader("token"))) {
            JSONArray problems = addProblemStatus(problem);
            problem = problems.getJSONObject(0);
        }
        if (problem != null) {
            result.success(problem, "查询成功");
        } else {
            result.error("该问题不存在");
        }
        return result;
    }


    public JSONArray addProblemStatus(Object data) {
        JSONArray problems = new JSONArray();
        if (data.getClass() == JSONObject.class) {
            problems.add(data);
        } else {
            problems = JSON.parseArray(JSON.toJSONString(data));
        }
        UserProfile profile = userProfileService.getByUserId(JwtUtil.getUserId(request.getHeader("token")));
        JSONObject oiStatus = profile.getOiProblemsStatus();
        JSONObject acmStatus = profile.getAcmProblemsStatus();
        for (int i = 0; i < problems.size(); i++) {
            JSONObject problem = problems.getJSONObject(i);
            String ruleType = problem.containsKey("ruleType") ? problem.getString("ruleType") : problem.getString("rule_type");
            String problemId = problem.getString("id");
            JSONObject problemInfo;
            if (oiStatus.containsKey(problemId) && ruleType.equals("OI")) {
                problemInfo = JSON.parseObject(oiStatus.getString(problemId));
                Integer status = problemInfo.getInteger("status");
                problem.put("myStatus", status);
                problems.set(i, problem);
            } else if (acmStatus.containsKey(problemId) && ruleType.equals("ACM")) {
                problemInfo = JSON.parseObject(acmStatus.getString(problemId));
                Integer status = problemInfo.getInteger("status");
                problem.put("myStatus", status);
                problems.set(i, problem);
            }
        }
        return problems;
    }


    @GetMapping("list")
    public Result<Page<JSONObject>> page(Integer limit, Integer page, String keyword, String difficulty, String tag) {
        Result<Page<JSONObject>> result = new Result<>();
        //tag功能未实现
        Page<JSONObject> problemPage = problemService.page(page, limit, keyword, difficulty, tag);
        List<JSONObject> records = problemPage.getRecords();
        if (JwtUtil.verifyToken(request.getHeader("token"))) {
            records = JSON.parseArray(addProblemStatus(records).toJSONString(), JSONObject.class);
            problemPage.setRecords(records);
        }
        result.success(problemPage, "查询成功");
        return result;
    }

}
