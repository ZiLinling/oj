package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.OiContestRank;
import com.xmut.onlinejudge.service.OiContestRankService;
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
@RequestMapping("/oiContest")
public class OiContestRankController {

    @Autowired
    private OiContestRankService oiContestRankService;

    @Autowired
    private RedisUtil redisUtil;


    @GetMapping("rank")
    private Result<Page<OiContestRank>> page(Integer limit, Integer page, Integer contestId) {
        Result<Page<OiContestRank>> result = new Result<>();
        Page<OiContestRank> oiContestRankPage = (Page<OiContestRank>) redisUtil.hget("contest_rank_cache:" + contestId, String.valueOf(page));
        if (oiContestRankPage == null) {
            oiContestRankPage = oiContestRankService.page(page, limit, contestId);
            redisUtil.hset("contest_rank_cache:" + contestId, String.valueOf(page), oiContestRankPage);
        }
        result.success(oiContestRankPage, "查询成功");
        return result;
    }

}
