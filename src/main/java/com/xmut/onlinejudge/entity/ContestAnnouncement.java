package com.xmut.onlinejudge.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Table(value = "contest_announcement")
public class ContestAnnouncement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String title;

    private String content;

    private Timestamp createTime;

    private Integer contestId;

    private Integer createdById;

    private Boolean visible;

}
