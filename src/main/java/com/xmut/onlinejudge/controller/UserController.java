package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.User;
import com.xmut.onlinejudge.service.UserService;
import com.xmut.onlinejudge.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

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


    /**
     * 添加。
     *
     * @param user
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return userService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param user
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public Result<List<User>> list() {
        Result result = new Result();
        result.setData(userService.list());
        return result;
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public User getInfo(@PathVariable Serializable id) {
        return userService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<User> page(Page<User> page) {
        return userService.page(page);
    }


    @PostMapping("login")
    public Result<User> login(@RequestBody User user) {
        Result<User> result = new Result<User>();
        User finduser = userService.findByName(user.getUsername());
        if (finduser != null) {
            if (finduser.getPassword().equals(user.getPassword())) {
                //更新最后登录时间
                finduser.setLastLogin(DateUtil.getCurrTime());
                result.success(user, "登录成功");
            } else {
                result.error("密码错误");
            }
        } else {
            result.error("用户名不存在");
        }
        return result;
    }

    @PostMapping("register")
    public Result<User> register(@RequestBody User user) {
        Result<User> result = new Result<User>();
        //设置账户类型
        user.setAdminType(0);
        // 设置账户状态
        user.setIsDisabled(false);
        //设置创建时间
        user.setCreateTime(DateUtil.getCurrTime());
        userService.add(user);
        result.success(null, "注册成功");
        return result;
    }

    @PostMapping("check_username_or_email")
    public Result<JSONObject> checkUsernameOrEmail(@RequestBody JSONObject data) {
        Result<JSONObject> result = new Result<JSONObject>();
        JSONObject judge = new JSONObject();
        judge.put("username", false);
        judge.put("email", false);
        if (userService.findName((String) data.get("username"))) {
            //用户名重复
            judge.replace("username", true);
        } else if (userService.findName((String) data.get("email"))) {
            //邮箱重复
            judge.replace("email", true);
        }
        result.success(judge, "判断用户名或邮箱是否重复");
        return result;
    }

}
