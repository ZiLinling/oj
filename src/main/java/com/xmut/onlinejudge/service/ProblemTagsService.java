package com.xmut.onlinejudge.service;

import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.ProblemTags;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface ProblemTagsService extends IService<ProblemTags> {

    void deleteByProblemId(Integer problemId);

}
