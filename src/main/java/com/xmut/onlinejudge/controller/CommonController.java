package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONObject;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.service.ContestService;
import com.xmut.onlinejudge.service.JudgeServerService;
import com.xmut.onlinejudge.service.SubmissionService;
import com.xmut.onlinejudge.service.UserService;
import com.xmut.onlinejudge.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private SubmissionService submissionService;

    @Autowired
    private ContestService contestService;

    @Autowired
    private JudgeServerService judgeServerService;

    @Autowired
    private UserService userService;

    @GetMapping("dashboard_info")
    public Result<JSONObject> getDashboardInfo() {
        Result<JSONObject> result = new Result<>();
        String today = DateUtil.getCurrDay();
        String currentTime = DateUtil.getCurrTime();
        long todaySubmissionCount = submissionService.countTodaySubmissions(today);
        long recentContestCount = contestService.countRecentContests(currentTime);
        long judgeServerCount = judgeServerService.countNormalJudgeServers();

        JSONObject env = new JSONObject();
        env.put("FORCE_HTTPS", false); // Assuming FORCE_HTTPS is a boolean
        env.put("STATIC_CDN_HOST", ""); // Assuming STATIC_CDN_HOST is a string

        JSONObject data = new JSONObject();
        data.put("user_count", userService.count());
        data.put("recent_contest_count", recentContestCount);
        data.put("today_submission_count", todaySubmissionCount);
        data.put("judge_server_count", judgeServerCount);
        data.put("env", env);

        result.setData(data);
        return result;
    }
}

