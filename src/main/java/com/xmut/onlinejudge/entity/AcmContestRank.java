package com.xmut.onlinejudge.entity;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.annotation.*;
import com.mybatisflex.core.handler.FastjsonTypeHandler;
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
@Table(value = "acm_contest_rank")
public class AcmContestRank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(onInsertValue = "0")
    private Integer submissionNumber;

    @Column(onInsertValue = "0")
    private Integer acceptedNumber;

    @Column(onInsertValue = "0")
    private Integer totalTime;

    @Column(onInsertValue = "'{}'", typeHandler = FastjsonTypeHandler.class)
    private JSONObject submissionInfo;

    private Integer contestId;

    private Integer userId;

    @RelationOneToOne(selfField = "userId", targetField = "id")
    private User user;

    public AcmContestRank(Integer contestId, Integer userId) {
        this.contestId = contestId;
        this.userId = userId;
        this.submissionNumber = 0;
        this.acceptedNumber = 0;
        this.totalTime = 0;
        this.submissionInfo = new JSONObject();
    }
}
