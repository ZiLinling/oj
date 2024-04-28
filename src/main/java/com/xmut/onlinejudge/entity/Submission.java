package com.xmut.onlinejudge.entity;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.FastjsonTypeHandler;
import com.mybatisflex.core.keygen.KeyGenerators;
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

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private String id;

    private Integer contestId;

    private Integer problemId;

    @Column(ignore = true)
    private String problemDisplayId;

    private String createTime;

    private Integer userId;

    private String code;

    @Column(onInsertValue = "6")
    private Integer result;

    @Column(onInsertValue = "'{}'", typeHandler = FastjsonTypeHandler.class)
    private JSONObject info;

    private String language;

    @Column(onInsertValue = "false")
    private Boolean shared;

    @Column(onInsertValue = "'{}'", typeHandler = FastjsonTypeHandler.class)
    private JSONObject statisticInfo;

    private String username;

    private String ip;

}
