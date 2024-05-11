package com.xmut.onlinejudge.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.xmut.onlinejudge.utils.DateUtil;
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
@Table(value = "judge_server")
public class JudgeServer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String ip;

    private String hostname;

    private String judgerVersion;

    private Integer cpuCore;

    private Double memoryUsage;

    private Double cpuUsage;
    private String lastHeartbeat;

    private String createTime;

    private Integer taskNumber;

    private String serviceUrl;

    private Boolean isDisabled;

    @Column(ignore = true)
    private String status;


    public String getStatus() {
        if (status != null)
            return status;
        if (DateUtil.getDiffSeconds(lastHeartbeat, DateUtil.getCurrTime()) > 6) {
            return "abnormal";
        }
        return "normal";
    }
}
