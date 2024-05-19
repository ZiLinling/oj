package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.VO.ProblemWithTags;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.mapper.ProblemMapper;
import com.xmut.onlinejudge.service.ProblemService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.VO.table.ProblemWithTagsTableDef.PROBLEM_WITH_TAGS;



/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

    @Override
    public Page<ProblemWithTags> page(Integer pageNum, Integer pageSize, String keyword, String difficulty, String tag,
                                      Integer contestId, String ruleType, Boolean isAdmin) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (keyword != null && !keyword.equals("")) {
            queryWrapper.and(PROBLEM_WITH_TAGS.TITLE.like("%" + keyword + "%"))
                    .or(PROBLEM_WITH_TAGS.DISPLAY_ID.like("%" + keyword + "%"));
        }
        if (difficulty != null && !difficulty.equals("")) {
            queryWrapper.and(PROBLEM_WITH_TAGS.DIFFICULTY.eq(difficulty));
        }
        if (tag != null && !tag.equals("")) {
            queryWrapper.and(PROBLEM_WITH_TAGS.TAGS.like("%\"" + tag + "\"%"));
        }
        if (ruleType != null && !ruleType.equals("")) {
            queryWrapper.and(PROBLEM_WITH_TAGS.RULE_TYPE.eq(ruleType));
        }
        if (contestId != null) {
            queryWrapper.and(PROBLEM_WITH_TAGS.CONTEST_ID.eq(contestId));
        } else {
            queryWrapper.and(PROBLEM_WITH_TAGS.CONTEST_ID.isNull());
        }
        if (!isAdmin) {
            queryWrapper.and(PROBLEM_WITH_TAGS.VISIBLE.eq(true));
            queryWrapper.orderBy(PROBLEM_WITH_TAGS.DISPLAY_ID, true);
        } else {
            queryWrapper.orderBy(PROBLEM_WITH_TAGS.ID, true);
        }
        queryWrapper.from(PROBLEM_WITH_TAGS);
        return this.mapper.paginateAs(pageNum, pageSize, queryWrapper, ProblemWithTags.class);

    }

    @Override
    public ProblemWithTags getByDisplayId(String displayId, Integer contestId, Boolean isAdmin) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(PROBLEM_WITH_TAGS.DISPLAY_ID.eq(displayId));
        queryWrapper.from(PROBLEM_WITH_TAGS);
        if (contestId != null) {
            queryWrapper.and(PROBLEM_WITH_TAGS.CONTEST_ID.eq(contestId));
        } else {
            queryWrapper.and(PROBLEM_WITH_TAGS.CONTEST_ID.isNull());
        }
        if (!isAdmin) {
            queryWrapper.and(PROBLEM_WITH_TAGS.VISIBLE.eq(true));
        }
        return this.mapper.selectOneByQueryAs(queryWrapper, ProblemWithTags.class);
    }

    @Override
    public ProblemWithTags getById(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(PROBLEM_WITH_TAGS.ID.eq(id));
        queryWrapper.from(PROBLEM_WITH_TAGS);
        return this.mapper.selectOneByQueryAs(queryWrapper, ProblemWithTags.class);
    }
}
