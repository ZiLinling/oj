package com.xmut.onlinejudge.service;

import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.User;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-03
 */
public interface UserService extends IService<User> {

    User getByUsername(String username);

    Boolean isUsernameExist(String username);

    Boolean isEmailExist(String email);

}
