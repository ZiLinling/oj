package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.mapper.ProblemMapper;
import com.xmut.onlinejudge.service.ProblemService;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

}
