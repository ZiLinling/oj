package com.xmut.onlinejudge.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.row.Row;
import com.xmut.onlinejudge.entity.Problem;

/**
 * 映射层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface ProblemMapper extends BaseMapper<Problem> {

    Row listForUser();


    Row listForAdmin();

}
