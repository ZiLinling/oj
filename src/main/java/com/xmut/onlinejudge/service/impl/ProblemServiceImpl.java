package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.mapper.ProblemMapper;
import com.xmut.onlinejudge.service.ProblemService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
    public Page<Row> page(Integer pageNum, Integer pageSize, String keyword, String difficulty, String tag) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Page<Row> page = new Page<>(pageNum, pageSize);
        queryWrapper.where(PROBLEM.VISIBLE.eq(true));
        if (keyword != null && !keyword.equals("")) {
            queryWrapper.and(PROBLEM.TITLE.like("%" + keyword + "%"));
        }
        if (difficulty != null && !difficulty.equals("")) {
            queryWrapper.and(PROBLEM.DIFFICULTY.eq(difficulty));
        }
        Map<String, Object> otherParams = new HashMap<>();
        System.out.println(tag);
        otherParams.put("tag", tag);

        return this.mapper.xmlPaginate("listForUser", Page.of(pageNum, pageSize), queryWrapper, otherParams);
    }

}
