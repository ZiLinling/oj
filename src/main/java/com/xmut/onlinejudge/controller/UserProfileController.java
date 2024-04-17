package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    /**
     * 添加。
     *
     * @param userProfile
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody UserProfile userProfile) {
        return userProfileService.save(userProfile);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return userProfileService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param userProfile
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody UserProfile userProfile) {
        return userProfileService.updateById(userProfile);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<UserProfile> list() {
        return userProfileService.list();
    }


    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<UserProfile> page(Page<UserProfile> page) {
        return userProfileService.page(page);
    }

    @GetMapping("/getInfo")
    public Result<UserProfile> getByUsername(String username) {
        Result<UserProfile> result = new Result<>();
        UserProfile userProfile = userProfileService.findByName(username);
        if (userProfile != null) {
            result.success(userProfile, "获取用户信息成功");
        } else {
            result.success(null, "获取用户信息失败");
        }
        return result;
    }

}
