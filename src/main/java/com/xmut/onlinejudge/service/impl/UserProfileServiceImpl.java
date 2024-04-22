package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.mapper.UserProfileMapper;
import com.xmut.onlinejudge.service.UserProfileService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.UserProfileTableDef.USER_PROFILE;
import static com.xmut.onlinejudge.entity.table.UserTableDef.USER;


/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {

    @Override
    public UserProfile findByName(String username) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(USER_PROFILE.ALL_COLUMNS).from(USER).join(USER_PROFILE).on(USER_PROFILE.USER_ID.eq(USER.ID)).where(USER.USERNAME.eq("username"));
        return this.mapper.selectOneByQuery(queryWrapper);
    }
}
