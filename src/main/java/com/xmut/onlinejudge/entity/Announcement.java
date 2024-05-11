package com.xmut.onlinejudge.entity;

import com.mybatisflex.annotation.Column;
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
@Table(value = "announcement")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String title;

    private String content;

    @Column(onInsertValue = "to_char(now(), 'YYYY-MM-DD HH24:MI:SS')")
    private String createTime;

    @Column(onInsertValue = "to_char(now(), 'YYYY-MM-DD HH24:MI:SS')")
    private String lastUpdateTime;

    private Boolean visible;

    private Integer createdById;

    @Column(onInsertValue = "false")
    private Boolean contest;

    private Integer contestId;

    @Column(ignore = true)
    private String creator;

}
