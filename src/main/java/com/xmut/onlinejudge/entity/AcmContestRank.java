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
@Table(value = "acm_contest_rank")
public class AcmContestRank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private Integer submissionNumber;

    private Integer acceptedNumber;

    private Integer totalTime;

    private String submissionInfo;

    private Integer contestId;

    private Integer userId;

}
