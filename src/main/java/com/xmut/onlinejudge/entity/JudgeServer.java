package com.xmut.onlinejudge.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
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
@Table(value = "judge_server")
public class JudgeServer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String hostname;

    private String judgerVersion;

    private Integer cpuCore;

    private Double memory;

    private Double cpu;

    private String lastHeartbeat;

    private String createTime;

    private Integer taskNumber;

    private String serviceUrl;

    private Boolean isDisabled;

//    public JudgeServer(JSONObject data) {
//        this.hostname= data.getString("hostname");
//        this.cpuCore= data.getInteger("cpu_core");
//        this.memory= data.getDouble("memory");
//        this.cpu= data.getDouble("cpu");
//        this.judgerVersion= data.getString("judger_version");
//    }
}
