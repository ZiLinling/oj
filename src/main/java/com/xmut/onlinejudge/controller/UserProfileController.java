package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.User;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.service.UserProfileService;
import com.xmut.onlinejudge.service.UserService;
import com.xmut.onlinejudge.utils.FileUtil;
import com.xmut.onlinejudge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

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

    @Autowired
    private FileUtil fileUtil;


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
    public Result<UserProfile> update(@RequestBody UserProfile userProfile) {
        Result<UserProfile> result = new Result<>();
        userProfileService.updateById(userProfile);
        userProfile = userProfileService.getById(userProfile.getId());
        User user = userService.getById(userProfile.getId());
        user.setPassword(null);
        userProfile.setUser(user);
        result.success(userProfile, "更新成功");
        return result;
    }


    @GetMapping("")
    public Result<UserProfile> getByUsername(@RequestParam(required = false) String username) {
        Result<UserProfile> result = new Result<>();
        if (username == null) {
            String token = request.getHeader("token");
            if (JwtUtil.verifyToken(token)) {
                username = JwtUtil.getUsername(token);
            } else {
                result.success(null, "尚未登录");
                return result;
            }
        }
        User user = userService.getByUsernameWithoutPassword(username);
        if (user == null || user.getIsDisabled()) {
            result.success(null, "获取用户信息失败");
        } else {
            UserProfile userProfile = userProfileService.getByUserId(user.getId());
            if (userProfile != null) {
                userProfile.setUser(user);
                result.success(userProfile, "获取用户信息成功");
            } else {
                result.success(null, "获取用户信息失败");
            }
        }
        return result;
    }

    @PostMapping("upload_avatar")
    public Result<String> uploadAvatar(@RequestParam("image") MultipartFile file, @RequestHeader("token") String token) {
        Result<String> result = new Result<>();
        if (!JwtUtil.verifyToken(token)) {
            result.error("Invalid token");
            return result;
        }
        Result<String> uploadResult = fileUtil.uploadAvatar(file);
        if (uploadResult.getError() == null) {
            UserProfile userProfile = userProfileService.getByUserId(JwtUtil.getUserId(token));
            userProfile.setAvatar(uploadResult.getData());
            userProfileService.updateById(userProfile);
            result.success(null, "Success");
        } else {
            result.error("Failed to uploadAvatar");
        }
        return result;
    }

    @GetMapping("user_rank")
    public Result<Page<UserProfile>> getRank(Integer limit, Integer page, String rule) {
        Result<Page<UserProfile>> result = new Result<>();
        Page<UserProfile> userProfilePage = userProfileService.page(page, limit, rule);
        result.success(userProfilePage, "查询成功");
        return result;
    }

}
