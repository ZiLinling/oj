package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.OiContestRank;
import com.xmut.onlinejudge.mapper.OiContestRankMapper;
import com.xmut.onlinejudge.service.OiContestRankService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.OiContestRankTableDef.OI_CONTEST_RANK;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class OiContestRankServiceImpl extends ServiceImpl<OiContestRankMapper, OiContestRank> implements OiContestRankService {
    @Override
    public OiContestRank getByUserIdAndContestId(Integer userId, Integer contestId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(OI_CONTEST_RANK.USER_ID.eq(userId)).and(OI_CONTEST_RANK.CONTEST_ID.eq(contestId));
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<OiContestRank> page(Integer pageNum, Integer pageSize, Integer contestId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(OI_CONTEST_RANK.CONTEST_ID.eq(contestId));
        queryWrapper.orderBy(OI_CONTEST_RANK.TOTAL_SCORE, false);
        return this.mapper.paginateWithRelations(pageNum, pageSize, queryWrapper);
    }
}
