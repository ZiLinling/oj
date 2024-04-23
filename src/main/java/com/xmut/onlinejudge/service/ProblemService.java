package com.xmut.onlinejudge.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.Problem;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface ProblemService extends IService<Problem> {

    Page<Row> page(Integer pageNum, Integer pageSize, String keyword, String difficulty, String tag);
}
