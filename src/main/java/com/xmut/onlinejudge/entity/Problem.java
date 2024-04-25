package com.xmut.onlinejudge.entity;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.FastjsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

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
@Table(value = "problem")
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String title;

    private String description;

    private String inputDescription;

    private String outputDescription;

    @Column(typeHandler = FastjsonTypeHandler.class)
    private List<JSONObject> samples;

    private String testCaseId;

    private String testCaseScore;

    private String hint;

    @Column(typeHandler = FastjsonTypeHandler.class)
    private List<String> languages;

    private String template;

    private Timestamp createTime;

    private Timestamp lastUpdateTime;

    private Integer timeLimit;

    private Integer memoryLimit;

    private Boolean spj;

    private String spjLanguage;

    private String spjCode;

    private String spjVersion;

    private String ruleType;

    private Boolean visible;

    private String difficulty;

    private String source;

    private Long submissionNumber;

    private Long acceptedNumber;

    private Integer createdById;

    @Column(value = "_id")
    private String _id;

    private String statisticInfo;

    private Integer totalScore;

    private Integer contestId;

    private Boolean isPublic;

    private Boolean spjCompileOk;

    @Column(typeHandler = FastjsonTypeHandler.class)
    private JSONObject ioMode;

    private Boolean shareSubmission;

}
