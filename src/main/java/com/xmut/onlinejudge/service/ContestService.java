package com.xmut.onlinejudge.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.VO.ContestWithStatus;
import com.xmut.onlinejudge.entity.Contest;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface ContestService extends IService<Contest> {

    Page<ContestWithStatus> page(Integer pageNum, Integer pageSize, Integer status, String keyword, Boolean isAdmin);

    Long countRecentContests(String currentTime);

    ContestWithStatus getByIdWithStatus(Integer id);
}
