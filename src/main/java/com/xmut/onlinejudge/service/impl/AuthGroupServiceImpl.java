package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.AuthGroup;
import com.xmut.onlinejudge.mapper.AuthGroupMapper;
import com.xmut.onlinejudge.service.AuthGroupService;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class AuthGroupServiceImpl extends ServiceImpl<AuthGroupMapper, AuthGroup> implements AuthGroupService {

}
