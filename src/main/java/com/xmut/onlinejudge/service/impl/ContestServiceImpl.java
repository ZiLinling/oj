package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.VO.ContestWithStatus;
import com.xmut.onlinejudge.entity.Contest;
import com.xmut.onlinejudge.mapper.ContestMapper;
import com.xmut.onlinejudge.service.ContestService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.VO.table.ContestWithStatusTableDef.CONTEST_WITH_STATUS;
import static com.xmut.onlinejudge.entity.table.ContestTableDef.CONTEST;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest> implements ContestService {

    @Override
    public Page<ContestWithStatus> page(Integer pageNum, Integer pageSize, Integer status, String keyword, Boolean isAdmin) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (status != null) {
            queryWrapper.where(CONTEST_WITH_STATUS.STATUS.eq(status));
        }
        if (keyword != null) {
            queryWrapper.where(CONTEST_WITH_STATUS.TITLE.like("%" + keyword + "%"));
        }
        if (!isAdmin) {
            queryWrapper.and(CONTEST_WITH_STATUS.VISIBLE.eq(true));
        }
        queryWrapper.orderBy(CONTEST_WITH_STATUS.START_TIME, false);
        queryWrapper.from(CONTEST_WITH_STATUS);
        return this.mapper.paginateAs(pageNum, pageSize, queryWrapper, ContestWithStatus.class);
    }

    @Override
    public Long countRecentContests(String currentTime) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(CONTEST.END_TIME.gt(currentTime));
        return this.count(queryWrapper);
    }

    @Override
    public ContestWithStatus getByIdWithStatus(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(CONTEST_WITH_STATUS.ALL_COLUMNS);
        queryWrapper.from(CONTEST_WITH_STATUS);
        queryWrapper.where(CONTEST_WITH_STATUS.ID.eq(id));
        return this.mapper.selectOneByQueryAs(queryWrapper, ContestWithStatus.class);
    }
}
