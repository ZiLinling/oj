package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.User;
import com.xmut.onlinejudge.service.UserService;
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
                result.success("登录成功");
                //进行操作

            } else {
                result.fail("密码错误");
            }
        } else {
            result.fail("用户名不存在");
        }
        return result;
    }

    @PostMapping("register")
    public Result<User> register(@RequestBody User user) {
        Result<User> result = new Result<User>();
        if (userService.findByName(user.getUsername()) != null) {
            result.fail("用户名已存在");
        } else {
            user.setAdminType(0);//普通用户
            user.setIsDisabled(false);//未禁用
            userService.add(user);
            result.success("注册成功");
        }
        return result;
    }

}
