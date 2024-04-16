package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.mapper.UserProfileMapper;
import com.xmut.onlinejudge.service.UserProfileService;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {

}
