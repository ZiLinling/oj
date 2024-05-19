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
@Table(value = "user_profile")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(onInsertValue = "'{}'", typeHandler = FastjsonTypeHandler.class)
    private JSONObject acmProblemsStatus;

    @Column(onInsertValue = "/public/avatar/default.png")
    private String avatar;

    private String blog;

    private String mood;

    @Column(onInsertValue = "0")
    private Integer acceptedNumber;

    @Column(onInsertValue = "0")
    private Integer submissionNumber;

    private String github;

    private String school;

    private String major;

    private Integer userId;

    @Column(onInsertValue = "0")
    private Long totalScore;

    @Column(onInsertValue = "'{}'", typeHandler = FastjsonTypeHandler.class)
    private JSONObject oiProblemsStatus;

    private String realName;

    private String language;

    @RelationOneToOne(selfField = "userId", targetField = "id")
    private User user;

    private String studentId;

    public void addScore(int score) {
        this.totalScore += score;
    }

    public void addScore(int curScore, int lastScore) {
        this.totalScore -= lastScore;
        this.totalScore += curScore;
    }
}
