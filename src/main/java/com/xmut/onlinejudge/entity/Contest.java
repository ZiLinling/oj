package com.xmut.onlinejudge.entity;

import com.alibaba.fastjson.JSONArray;
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
@Table(value = "contest")
public class Contest implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PUBLIC_CONTEST = "Public";
    private static final String PASSWORD_PROTECTED_CONTEST = "Password Protected";


    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String title;

    private String description;

    private Boolean realTimeRank;

    private String password;

    private String ruleType;

    private String startTime;

    private String endTime;

    @Column(onInsertValue = "to_char(now(), 'YYYY-MM-DD HH24:MI:SS')")
    private String createTime;

    @Column(onUpdateValue = "to_char(now(), 'YYYY-MM-DD HH24:MI:SS')", onInsertValue = "to_char(now(), 'YYYY-MM-DD HH24:MI:SS')")
    private String lastUpdateTime;

    private Boolean visible;

    private Integer createdById;

    @Column(typeHandler = FastjsonTypeHandler.class)
    private JSONArray allowedIpRanges;

    private String contestType;

    public void updateContestType() {
        if (password != null && !password.isEmpty())
            contestType = PASSWORD_PROTECTED_CONTEST;
        else
            contestType = PUBLIC_CONTEST;
    }
}
