package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Announcement;
import com.xmut.onlinejudge.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 添加。
     *
     * @param announcement
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Announcement announcement) {
        return announcementService.save(announcement);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return announcementService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param announcement
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Announcement announcement) {
        return announcementService.updateById(announcement);
    }



    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public Announcement getInfo(@PathVariable Serializable id) {
        return announcementService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */


    @GetMapping("list")
    public Result<Page<Announcement>> page(Integer limit, Integer page) {
        Result<Page<Announcement>> result = new Result<>();
        Page<Announcement> announcementPage = new Page<>(page, limit);
        announcementPage = announcementService.page(announcementPage);
        result.success(announcementPage, "查询成功");
        return result;
    }

}
