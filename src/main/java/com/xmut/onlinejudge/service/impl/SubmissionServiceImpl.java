package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.Submission;
import com.xmut.onlinejudge.mapper.SubmissionMapper;
import com.xmut.onlinejudge.service.SubmissionService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.ProblemTableDef.PROBLEM;
import static com.xmut.onlinejudge.entity.table.SubmissionTableDef.SUBMISSION;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements SubmissionService {

    @Override
    public Boolean isExist(Integer problemId, Integer userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(SUBMISSION.PROBLEM_ID.eq(problemId)).and(SUBMISSION.USER_ID.eq(userId));
        return this.exists(queryWrapper);
    }

    @Override
    public Page<Submission> page(Integer pageNum, Integer pageSize, String username, Integer result, Integer userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(SUBMISSION.ALL_COLUMNS, PROBLEM.DISPLAY_ID.as("problemDisplayId")).from(SUBMISSION).join(PROBLEM).on(SUBMISSION.PROBLEM_ID.eq(PROBLEM.ID));
        if (username != null && !username.equals("")) {
            queryWrapper.and(SUBMISSION.USERNAME.like("%" + username + "%"));
        }
        if (result != null) {
            queryWrapper.and(SUBMISSION.RESULT.eq(result));
        }
        if (userId != null) {
            queryWrapper.and(SUBMISSION.USER_ID.eq(userId));
        }
        queryWrapper.orderBy("create_time", false);
        return this.mapper.paginate(Page.of(pageNum, pageSize), queryWrapper);
    }
}
