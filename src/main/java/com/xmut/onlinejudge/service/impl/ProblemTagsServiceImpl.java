package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.ProblemTags;
import com.xmut.onlinejudge.mapper.ProblemTagsMapper;
import com.xmut.onlinejudge.service.ProblemTagsService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.ProblemTagsTableDef.PROBLEM_TAGS;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class ProblemTagsServiceImpl extends ServiceImpl<ProblemTagsMapper, ProblemTags> implements ProblemTagsService {

    @Override
    public void deleteByProblemId(Integer problemId) {
        this.remove(PROBLEM_TAGS.PROBLEM_ID.eq(problemId));
    }
}
