package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.AuthPermission;
import com.xmut.onlinejudge.mapper.AuthPermissionMapper;
import com.xmut.onlinejudge.service.AuthPermissionService;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class AuthPermissionServiceImpl extends ServiceImpl<AuthPermissionMapper, AuthPermission> implements AuthPermissionService {

}
