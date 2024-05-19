package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.mapper.UserProfileMapper;
import com.xmut.onlinejudge.service.UserProfileService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.UserProfileTableDef.USER_PROFILE;


/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {

    @Override
    public UserProfile getByUserId(Integer userId) {
        return this.getOne(USER_PROFILE.USER_ID.eq(userId));
    }

    @Override
    public Page<UserProfile> page(Integer pageNum, Integer pageSize, String rule) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (rule.equals("ACM")) {
            queryWrapper.orderBy(USER_PROFILE.ACCEPTED_NUMBER, false);
        } else {
            queryWrapper.orderBy(USER_PROFILE.TOTAL_SCORE, false);
        }
        return this.mapper.paginateWithRelations(Page.of(pageNum, pageSize), queryWrapper);
    }

    @Override
    public void removeByUserId(Integer userId) {
        this.mapper.deleteByCondition(USER_PROFILE.USER_ID.eq(userId));
    }
}
