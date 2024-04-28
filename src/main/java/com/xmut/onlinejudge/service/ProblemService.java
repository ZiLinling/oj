package com.xmut.onlinejudge.service;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.Problem;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface ProblemService extends IService<Problem> {

    Page<JSONObject> page(Integer pageNum, Integer pageSize, String keyword, String difficulty, String tag);

    Problem getByDisplayId(String displayId);
}
