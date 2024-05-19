package com.xmut.onlinejudge.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.AcmContestRank;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface AcmContestRankService extends IService<AcmContestRank> {

    AcmContestRank getByUserIdAndContestId(Integer userId, Integer contestId);

    Page<AcmContestRank> page(Integer pageNum, Integer pageSize, Integer contestId);
}
