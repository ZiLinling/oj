package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.entity.AuthPermission;
import com.xmut.onlinejudge.service.AuthPermissionService;
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
@RequestMapping("/authPermission")
public class AuthPermissionController {

    @Autowired
    private AuthPermissionService authPermissionService;

    /**
     * 添加。
     *
     * @param authPermission
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AuthPermission authPermission) {
        return authPermissionService.save(authPermission);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return authPermissionService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param authPermission
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AuthPermission authPermission) {
        return authPermissionService.updateById(authPermission);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AuthPermission> list() {
        return authPermissionService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public AuthPermission getInfo(@PathVariable Serializable id) {
        return authPermissionService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AuthPermission> page(Page<AuthPermission> page) {
        return authPermissionService.page(page);
    }

}
