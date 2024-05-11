package com.xmut.onlinejudge.entity;

import com.alibaba.fastjson.JSONArray;
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
    private JSONArray samples;

    private String testCaseId;

    @Column(typeHandler = FastjsonTypeHandler.class)
    private JSONArray testCaseScore;

    private String hint;

    @Column(typeHandler = FastjsonTypeHandler.class)
    private List<String> languages;

    @Column(onInsertValue = "'{}'", typeHandler = FastjsonTypeHandler.class)
    private JSONObject template;

    @Column(onInsertValue = "to_char(now(), 'YYYY-MM-DD HH24:MI:SS')")
    private String createTime;

    private String lastUpdateTime;

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

    @Column(onInsertValue = "0")
    private Long submissionNumber;

    @Column(onInsertValue = "0")
    private Long acceptedNumber;

    private Integer createdById;

    @Column(value = "_id")
    private String displayId;

    @Column(onInsertValue = "'{}'", typeHandler = FastjsonTypeHandler.class)
    private JSONObject statisticInfo;

    private Integer totalScore;

    private Integer contestId;

    @Column(onInsertValue = "false")
    private Boolean isPublic;

    private Boolean spjCompileOk;

    @Column(typeHandler = FastjsonTypeHandler.class)
    private JSONObject ioMode;

    private Boolean shareSubmission;


}
