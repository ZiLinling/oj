package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.User;
import com.xmut.onlinejudge.mapper.UserMapper;
import com.xmut.onlinejudge.service.UserService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.UserTableDef.USER;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    public User getByUsernameWithoutPassword(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(USER.ID, USER.USERNAME, USER.EMAIL, USER.ADMIN_TYPE, USER.IS_DISABLED, USER.CREATE_TIME);
        queryWrapper.where(USER.USERNAME.eq(username));
        return this.mapper.selectOneByQuery(queryWrapper);
    }

    public Boolean isUsernameExist(String username) {
        User user = this.mapper.selectOneByCondition(USER.USERNAME.eq(username));
        return user != null;
    }

    public Boolean isEmailExist(String email) {
        User user = this.mapper.selectOneByCondition(USER.EMAIL.eq(email));
        return user != null;
    }

    public User getByUsernameWithPassword(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(USER.USERNAME.eq(username));
        return this.mapper.selectOneByQuery(queryWrapper);
    }

}
