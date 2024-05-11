package com.xmut.onlinejudge.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.VO.ProblemWithTags;
import com.xmut.onlinejudge.entity.Problem;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface ProblemService extends IService<Problem> {

    Page<ProblemWithTags> page(Integer pageNum, Integer pageSize, String keyword, String difficulty, String tag, Boolean isAdmin);

    ProblemWithTags getByDisplayId(String displayId, Boolean isAdmin);

    ProblemWithTags getById(Integer Id);

}
