package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Announcement;
import com.xmut.onlinejudge.service.AnnouncementService;
import com.xmut.onlinejudge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private HttpServletRequest request;

    @GetMapping("admin/list")
    public Result<Page<Announcement>> pageForAdmin(Integer page, Integer limit, Integer contestId) {
        Result<Page<Announcement>> result = new Result<>();
        Page<Announcement> announcementPage = announcementService.page(page, limit, contestId, true);
        result.success(announcementPage, "查询成功");
        return result;
    }

    @GetMapping("list")
    public Result<Page<Announcement>> pageForUser(Integer page, Integer limit, Integer contestId) {
        Result<Page<Announcement>> result = new Result<>();
        if (page == null) {
            page = 1;
        }
        if (limit == null) {
            limit = 1000;
        }
        Page<Announcement> announcementPage = announcementService.page(page, limit, contestId, false);
        result.success(announcementPage, "查询成功");
        return result;
    }

    @PostMapping("admin/save")
    public Result<Announcement> save(@RequestBody Announcement announcement) {
        Result<Announcement> result = new Result<>();
        String token = request.getHeader("token");
        Integer userId = JwtUtil.getUserId(token);
        announcement.setCreatedById(userId);
        if (announcementService.save(announcement)) {
            result.success(null, "添加成功");
        } else {
            result.error("添加失败");
        }
        return result;
    }

    @PutMapping("admin/update")
    public Result<Announcement> updateById(@RequestBody Announcement announcement) {
        Result<Announcement> result = new Result<>();
        if (announcementService.updateById(announcement)) {
            result.success(null, "更新成功");
        } else {
            result.error("更新失败");
        }
        return result;
    }

    @DeleteMapping("admin/delete")
    public Result<Announcement> removeById(Integer id) {
        Result<Announcement> result = new Result<>();
        if (announcementService.removeById(id)) {
            result.success(null, "删除成功");
        } else {
            result.error("删除失败");
        }
        return result;
    }

}
