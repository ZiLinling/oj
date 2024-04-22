package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.mapper.ProblemMapper;
import com.xmut.onlinejudge.service.ProblemService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.ProblemTableDef.PROBLEM;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

    @Override
    public Page<Problem> page(Integer pageNum, Integer pageSize, String keyword, String difficulty, String tag) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(PROBLEM.ALL_COLUMNS);
        if (keyword != null && !keyword.equals("")) {
            queryWrapper.and(PROBLEM.TITLE.like("%" + keyword + "%"));
        }
        if (difficulty != null && !difficulty.equals("")) {
            queryWrapper.and(PROBLEM.DIFFICULTY.eq(difficulty));
        }
        queryWrapper.orderBy("id");
        return this.mapper.paginate(pageNum, pageSize, queryWrapper);
    }

}
