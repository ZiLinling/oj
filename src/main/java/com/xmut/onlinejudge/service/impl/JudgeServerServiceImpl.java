package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.JudgeServer;
import com.xmut.onlinejudge.mapper.JudgeServerMapper;
import com.xmut.onlinejudge.service.JudgeServerService;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class JudgeServerServiceImpl extends ServiceImpl<JudgeServerMapper, JudgeServer> implements JudgeServerService {

}
