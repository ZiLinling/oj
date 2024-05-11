package com.xmut.onlinejudge.VO;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.FastjsonTypeHandler;
import com.xmut.onlinejudge.entity.Problem;
import lombok.Data;

import java.util.List;

/**
 * 实体类。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Data
@Table(value = "problem_with_tags")
public class ProblemWithTags extends Problem {

    @Column(typeHandler = FastjsonTypeHandler.class)
    private List<String> tags;

    @Column(ignore = true)
    private Integer myStatus;

    private String creator;

}
