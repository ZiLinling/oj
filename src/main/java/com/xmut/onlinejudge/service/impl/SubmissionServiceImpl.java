package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.Submission;
import com.xmut.onlinejudge.mapper.SubmissionMapper;
import com.xmut.onlinejudge.service.SubmissionService;
import org.springframework.stereotype.Service;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission> implements SubmissionService {

}
