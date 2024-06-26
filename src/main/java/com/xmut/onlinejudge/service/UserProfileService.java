package com.xmut.onlinejudge.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.UserProfile;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface UserProfileService extends IService<UserProfile> {

    UserProfile getByUserId(Integer userId);

    Page<UserProfile> page(Integer pageNum, Integer pageSize, String rule);

    void removeByUserId(Integer userId);

}
