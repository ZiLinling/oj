package com.xmut.onlinejudge.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 实体类。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "submission")
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Integer contestId;

    private Integer problemId;

    private String createTime;

    private Integer userId;

    private String code;

    private Integer result;

    private String info;

    private String language;

    private Boolean shared;

    private String statisticInfo;

    private String username;

    private String ip;

}
