package com.xmut.onlinejudge.service.impl;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xmut.onlinejudge.entity.Announcement;
import com.xmut.onlinejudge.mapper.AnnouncementMapper;
import com.xmut.onlinejudge.service.AnnouncementService;
import org.springframework.stereotype.Service;

import static com.xmut.onlinejudge.entity.table.AnnouncementTableDef.ANNOUNCEMENT;
import static com.xmut.onlinejudge.entity.table.UserTableDef.USER;

/**
 * 服务层实现。
 *
 * @author Zi
 * @since 2024-03-05
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Override
    public Page<Announcement> page(Integer pageNum, Integer pageSize, Boolean All) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(ANNOUNCEMENT.ALL_COLUMNS, USER.USERNAME.as("creator")).from(ANNOUNCEMENT).orderBy("create_time", false).join(USER).on(ANNOUNCEMENT.CREATED_BY_ID.eq(USER.ID));
        if (!All) {
            queryWrapper.where(ANNOUNCEMENT.VISIBLE.eq(true));
        }
        return this.mapper.paginate(pageNum, pageSize, queryWrapper);
    }
}
