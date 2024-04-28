package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.User;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.service.UserProfileService;
import com.xmut.onlinejudge.service.UserService;
import com.xmut.onlinejudge.utils.FileUtil;
import com.xmut.onlinejudge.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final String AVATAR_URI_PREFIX = "/public/avatar"; // 头像URI前缀
    @Value("${files.upload.avatar.path}")
    private String AVATAR_UPLOAD_DIR;

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
    public Result<JSONObject> getByUsername(@RequestParam(required = false) String username) {
        Result<JSONObject> result = new Result<>();
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
                //将userProfile转为json对象
                JSONObject data = (JSONObject) JSONObject.toJSON(userProfile);
                data.put("user", user);
                result.success(data, "获取用户信息成功");
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
        Result<String> uploadResult = FileUtil.upload(file, AVATAR_UPLOAD_DIR, AVATAR_URI_PREFIX);
        if (uploadResult.getError() == null) {
            UserProfile userProfile = userProfileService.getByUserId(JwtUtil.getUserId(token));
            userProfile.setAvatar(uploadResult.getData());
            userProfileService.updateById(userProfile);
            result.success(null, "Success");
        } else {
            result.error("Failed to upload");
        }
        return result;
    }
}
