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
@Table(value = "user_profile")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String acmProblemsStatus;

    private String avatar;

    private String blog;

    private String mood;

    private Integer acceptedNumber;

    private Integer submissionNumber;

    private String github;

    private String school;

    private String major;

    private Integer userId;

    private Long totalScore;

    private String oiProblemsStatus;

    private String realName;

    private String language;

}
