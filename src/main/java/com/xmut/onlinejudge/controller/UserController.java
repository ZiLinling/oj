package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.User;
import com.xmut.onlinejudge.entity.UserProfile;
import com.xmut.onlinejudge.service.UserProfileService;
import com.xmut.onlinejudge.service.UserService;
import com.xmut.onlinejudge.utils.DateUtil;
import com.xmut.onlinejudge.utils.EmailUtil;
import com.xmut.onlinejudge.utils.JwtUtil;
import com.xmut.onlinejudge.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private HttpServletRequest request;



    @PostMapping("login")
    public Result<String> login(@RequestBody User user) {
        Result<String> result = new Result<String>();
        User getter = userService.getByUsernameWithPassword(user.getUsername());
        if (getter != null) {
            if (getter.getPassword().equals(user.getPassword())) {
                if (getter.getIsDisabled()) {
                    result.error("你的账号已被禁用");
                }
                //更新最后登录时间
                result.success(JwtUtil.generateToken(getter), "登录成功");
            } else {
                result.error("密码错误");
            }
        } else {
            result.error("用户名不存在");
        }
        return result;
    }

    @GetMapping("logout")
    public Result<String> logout() {
        Result<String> result = new Result<String>();
        String token = request.getHeader("token");
        if (JwtUtil.verifyToken(token)) {
            Integer userId = JwtUtil.getUserId(token);
            User user = userService.getById(userId);
            user.setLastLogin(DateUtil.getCurrTime());
            userService.updateById(user);
        }
        result.success(null, "登出成功");
        return result;
    }

    @PostMapping("register")
    public Result<User> register(@RequestBody String data) {
        System.out.println(data);
        Result<User> result = new Result<User>();
        //将data中的user提取出来
        User user = JSON.parseObject(data, User.class);
        JSONObject jsonObject = JSONObject.parseObject(data);
        //将data中的captcha提取出来
        String captcha = jsonObject.getString("captcha");
        if (redisUtil.hasKey(user.getEmail()) && redisUtil.get(user.getEmail()).equals(captcha)) {
            //设置账户类型
            user.setAdminType(0);
            // 设置账户状态
            user.setIsDisabled(false);
            //设置创建时间
            user.setCreateTime(DateUtil.getCurrTime());
            //保存用户
            userService.save(user);
            //并创建用户档案
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getId());
            userProfileService.save(userProfile);
            result.success(null, "注册成功");
        } else {
            result.error("验证码错误");
        }
        return result;
    }

    //生成邮箱验证码
    @GetMapping("captcha")
    public Result<String> sendEmailCode(@RequestParam String email) throws Exception {
        Result<String> result = new Result<String>();
        if (redisUtil.get(email) != null) {
            result.error("验证码尚未超时,请勿频繁发送");
        } else {
            //生成6位String类型验证码
            String captcha = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
            //发送邮件
            emailUtil.send(email, captcha);
            redisUtil.set(email, captcha, 60 * 5);
            result.success(null, "发送验证码成功");
        }
        return result;
    }

    @PostMapping("check_username_or_email")
    public Result<JSONObject> checkUsernameOrEmail(@RequestBody JSONObject data) {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject judge = new JSONObject();
        String username = data.getString("username");
        String email = data.getString("email");
        judge.put("username", false);
        judge.put("email", false);
        if (username != null && userService.isUsernameExist(username)) {
            //用户名重复
            judge.replace("username", true);
        } else if (email != null && userService.isEmailExist(email)) {
            //邮箱重复
            judge.replace("email", true);
        }
        result.success(judge, "判断用户名或邮箱是否重复");
        return result;
    }

}
