package com.xmut.onlinejudge.service;

import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.ProblemTag;

import java.util.List;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface ProblemTagService extends IService<ProblemTag> {

    List<ProblemTag> getListByKeyword(String keyword);

    ProblemTag getByName(String name);

}
