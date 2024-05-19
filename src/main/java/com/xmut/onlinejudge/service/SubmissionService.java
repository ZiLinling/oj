package com.xmut.onlinejudge.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.Submission;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface SubmissionService extends IService<Submission> {

    Boolean isExist(Integer problemId, Integer userId);

    Page<Submission> page(Integer pageNum, Integer pageSize, Integer contestId, String username, Integer result, Integer userId, Integer problemId);

    Long countTodaySubmissions(String today);

}
