package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.service.UserProfileService;
import com.xmut.onlinejudge.service.UserService;
import com.xmut.onlinejudge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加。
     *
     * @param userProfile
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody UserProfile userProfile) {
        userProfile.setUserId(1);
        userProfileService.save(userProfile);
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

    @GetMapping("")
    public Result<JSONObject> getByToken() {
        Result<JSONObject> result = new Result<>();
        String token = request.getHeader("token");
        if (token != null) {
            Integer userId = JwtUtil.getUserId(token);
            UserProfile userProfile = userProfileService.getById(userId);
            if (userProfile != null) {
                //将userProfile转为json对象
                JSONObject data = (JSONObject) JSONObject.toJSON(userProfile);
                //删除userId
                data.remove("userId");
                //将user转为json对象
                JSONObject user = (JSONObject) JSONObject.toJSON(userService.getById(userId));
                //删除密码
                user.remove("password");
                //存入data
                data.put("user", user);
                result.success(data, "获取用户信息成功");
            } else {
                result.success(null, "获取用户信息失败");
            }
        } else {
            result.success(null, "获取用户信息失败");
        }
        return result;
    }

}
