package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.ProblemTag;
import com.xmut.onlinejudge.mapper.ProblemTagMapper;
import com.xmut.onlinejudge.service.ProblemTagService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xmut.onlinejudge.entity.table.ProblemTagTableDef.PROBLEM_TAG;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class ProblemTagServiceImpl extends ServiceImpl<ProblemTagMapper, ProblemTag> implements ProblemTagService {

    @Override
    public List<ProblemTag> getListByKeyword(String keyword) {
        return this.list(PROBLEM_TAG.NAME.like("%" + keyword + "%"));
    }

    @Override
    public ProblemTag getByName(String name) {
        return this.getOne(PROBLEM_TAG.NAME.eq(name));
    }
}
