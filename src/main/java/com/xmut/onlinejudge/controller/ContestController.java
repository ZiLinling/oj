package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.VO.ContestWithStatus;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Contest;
import com.xmut.onlinejudge.service.ContestService;
import com.xmut.onlinejudge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    private ContestService contestService;


    @GetMapping("list")
    public Result<Page<ContestWithStatus>> pageForUser(Integer page, Integer limit, Integer status, String keyword) {
        Result<Page<ContestWithStatus>> result = new Result<>();
        Page<ContestWithStatus> contestPage = contestService.page(page, limit, status, keyword, false);
        result.success(contestPage, "查询成功");
        return result;
    }

    @GetMapping("admin/list")
    public Result<Page<ContestWithStatus>> pageForAdmin(Integer page, Integer limit, Integer status, String keyword) {
        Result<Page<ContestWithStatus>> result = new Result<>();
        Page<ContestWithStatus> contestPage = contestService.page(page, limit, status, keyword, true);
        result.success(contestPage, "查询成功");
        return result;
    }

    @PostMapping("admin/save")
    public Result<Contest> save(@RequestBody Contest contest, @RequestHeader("token") String token) {
        Result<Contest> result = new Result<>();
        contest.setCreatedById(JwtUtil.getUserId(token));
        if (contest.getStartTime().compareTo(contest.getEndTime()) > 0) {
            result.error("开始时间不能大于结束时间");
            return result;
        }
        contest.updateContestType();
        contestService.save(contest);
        result.success(null, "创建成功");
        return result;
    }

    @PutMapping("admin/update")
    public Result<Contest> updateById(@RequestBody Contest contest) {
        Result<Contest> result = new Result<>();
        if (contest.getStartTime().compareTo(contest.getEndTime()) > 0) {
            result.error("开始时间不能大于结束时间");
            return result;
        }
        contest.updateContestType();
        contestService.updateById(contest);
        result.success(null, "更新成功");
        return result;
    }

    @GetMapping("admin/get")
    public Result<Contest> getByIdForAdmin(Integer id) {
        Result<Contest> result = new Result<>();
        Contest contest = contestService.getById(id);
        if (contest != null) {
            result.success(contest, "查询成功");
        } else {
            result.error("该问题不存在");
        }
        return result;
    }

    @GetMapping("get")
    public Result<ContestWithStatus> getByIdForUser(Integer id) {
        Result<ContestWithStatus> result = new Result<>();
        ContestWithStatus contest = contestService.getByIdWithStatus(id);
        if (contest != null) {
            contest.setPassword(null);
            result.success(contest, "查询成功");
        } else {
            result.error("该问题不存在");
        }
        return result;
    }

    @GetMapping("access")
    public Result<JSONObject> access(Integer contestId, @RequestParam(required = false) String password) {
        Result<JSONObject> result = new Result<>();
        Contest contest = contestService.getById(contestId);
        if (contest == null) {
            result.error("该比赛不存在");
            return result;
        }
        JSONObject data = new JSONObject();
        if (Objects.equals(contest.getPassword(), password)) {
            data.put("access", true);
            result.success(data, "密码正确");
        } else {
            data.put("access", false);
            result.success(data, "密码错误");
        }
        return result;
    }

}
