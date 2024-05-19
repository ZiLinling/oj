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
 * @since 2024-03-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(isLarge = true)
    private String password;

    private String lastLogin;

    private String username;

    private String email;

    @Column(onInsertValue = "to_char(now(), 'YYYY-MM-DD HH24:MI:SS')")
    private String createTime;

    @Column(onInsertValue = "'Regular User'")
    private String adminType;

    @Column(onInsertValue = "false")
    private Boolean isDisabled;

    private String problemPermission;

    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }
}
