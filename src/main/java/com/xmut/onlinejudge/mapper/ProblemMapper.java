package com.xmut.onlinejudge.mapper;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.row.Row;
import com.xmut.onlinejudge.entity.Problem;
import org.apache.ibatis.annotations.Param;

/**
 * 映射层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface ProblemMapper extends BaseMapper<Problem> {

    Row listForUser(@Param("tag") String tag);


    Row listForAdmin(@Param("tag") String tag);

}
