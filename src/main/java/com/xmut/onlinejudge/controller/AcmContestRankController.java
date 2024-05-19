package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.AcmContestRank;
import com.xmut.onlinejudge.service.AcmContestRankService;
import com.xmut.onlinejudge.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/acmContest")
public class AcmContestRankController {

    @Autowired
    private AcmContestRankService acmContestRankService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("rank")
    private Result<Page<AcmContestRank>> page(Integer limit, Integer page, Integer contestId) {
        Result<Page<AcmContestRank>> result = new Result<>();
        Page<AcmContestRank> acmContestRankPage = (Page<AcmContestRank>) redisUtil.hget("contest_rank_cache:" + contestId, String.valueOf(page));
        if (acmContestRankPage == null) {
            acmContestRankPage = acmContestRankService.page(page, limit, contestId);
            redisUtil.hset("contest_rank_cache:" + contestId, String.valueOf(page), acmContestRankPage);
        }
        result.success(acmContestRankPage, "查询成功");
        return result;
    }

}
