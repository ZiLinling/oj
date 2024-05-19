package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.AcmContestRank;
import com.xmut.onlinejudge.mapper.AcmContestRankMapper;
import com.xmut.onlinejudge.service.AcmContestRankService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.AcmContestRankTableDef.ACM_CONTEST_RANK;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class AcmContestRankServiceImpl extends ServiceImpl<AcmContestRankMapper, AcmContestRank> implements AcmContestRankService {

    @Override
    public AcmContestRank getByUserIdAndContestId(Integer userId, Integer contestId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(ACM_CONTEST_RANK.USER_ID.eq(userId)).and(ACM_CONTEST_RANK.CONTEST_ID.eq(contestId));
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<AcmContestRank> page(Integer pageNum, Integer pageSize, Integer contestId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(ACM_CONTEST_RANK.CONTEST_ID.eq(contestId));
        queryWrapper.orderBy(ACM_CONTEST_RANK.ACCEPTED_NUMBER, false).orderBy(ACM_CONTEST_RANK.TOTAL_TIME, true);
        return this.mapper.paginateWithRelations(pageNum, pageSize, queryWrapper);
    }
}
