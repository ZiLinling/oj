package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.Contest;
import com.xmut.onlinejudge.mapper.ContestMapper;
import com.xmut.onlinejudge.service.ContestService;
import com.xmut.onlinejudge.utils.DateUtil;
import org.springframework.stereotype.Service;

import static com.mybatisflex.core.query.QueryMethods.*;
import static com.xmut.onlinejudge.entity.table.ContestTableDef.CONTEST;
import static com.xmut.onlinejudge.entity.table.UserTableDef.USER;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest> implements ContestService {

    @Override
    public Page<Row> page(Integer pageNum, Integer pageSize, Integer status) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Page<Row> page = new Page<>(pageNum, pageSize);
        queryWrapper.with("contest_status").asSelect(
                select(CONTEST.ID,
                        CONTEST.TITLE, CONTEST.DESCRIPTION, CONTEST.CREATE_TIME,
                        CONTEST.START_TIME, CONTEST.END_TIME, CONTEST.REAL_TIME_RANK, CONTEST.RULE_TYPE,
                        CONTEST.LAST_UPDATE_TIME, CONTEST.CONTEST_TYPE,
                        USER.USERNAME.as("created_user")
                        , case_()
                                //当开始时间大于当前时间时，返回1,表示未开始
                                .when(CONTEST.START_TIME.gt(DateUtil.getCurrTime())).then(1)
                                //当结束时间小于当前时间时，返回-1,表示已结束
                                .when(CONTEST.END_TIME.lt(DateUtil.getCurrTime())).then(-1)
                                //其他情况返回0,表示正在进行
                                .else_(0)
                                .end().as("status")).from(CONTEST).orderBy("start_time").join(USER).on(CONTEST.CREATED_BY_ID.eq(USER.ID))
        ).select("*").from("contest_status");
        if (status != null) {
            queryWrapper.where(column("status").eq(status));
        }
        return this.pageAs(page, queryWrapper, Row.class);
    }

    @Override
    public Long countRecentContests(String currentTime) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(CONTEST.END_TIME.gt(currentTime));
        return this.count(queryWrapper);
    }
}
