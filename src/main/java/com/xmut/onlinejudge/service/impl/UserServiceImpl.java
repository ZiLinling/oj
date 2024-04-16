package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.User;
import com.xmut.onlinejudge.mapper.UserMapper;
import com.xmut.onlinejudge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserMapper userMapper;

    public User findByName(String username) {
        return userMapper.selectOneByCondition(USER.USERNAME.eq(username));
    }

    public Boolean add(User user) {
        int a = userMapper.insertSelective(user);
        System.out.println(a);
        return true;
    }
}
