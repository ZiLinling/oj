package com.xmut.onlinejudge.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.OiContestRank;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface OiContestRankService extends IService<OiContestRank> {

    OiContestRank getByUserIdAndContestId(Integer userId, Integer contestId);

    Page<OiContestRank> page(Integer pageNum, Integer pageSize, Integer contestId);

}
