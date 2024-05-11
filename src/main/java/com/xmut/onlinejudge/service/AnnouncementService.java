package com.xmut.onlinejudge.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;
import com.xmut.onlinejudge.entity.Announcement;

/**
 * 服务层。
 *
 * @author Zi
 * @since 2024-03-05
 */
public interface AnnouncementService extends IService<Announcement> {

    Page<Announcement> page(Integer pageNum, Integer pageSize, Boolean All);
}
