package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.entity.AuthGroupPermissions;
import com.xmut.onlinejudge.service.AuthGroupPermissionsService;
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
@RequestMapping("/authGroupPermissions")
public class AuthGroupPermissionsController {

    @Autowired
    private AuthGroupPermissionsService authGroupPermissionsService;

    /**
     * 添加。
     *
     * @param authGroupPermissions
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AuthGroupPermissions authGroupPermissions) {
        return authGroupPermissionsService.save(authGroupPermissions);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return authGroupPermissionsService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param authGroupPermissions
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AuthGroupPermissions authGroupPermissions) {
        return authGroupPermissionsService.updateById(authGroupPermissions);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AuthGroupPermissions> list() {
        return authGroupPermissionsService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public AuthGroupPermissions getInfo(@PathVariable Serializable id) {
        return authGroupPermissionsService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AuthGroupPermissions> page(Page<AuthGroupPermissions> page) {
        return authGroupPermissionsService.page(page);
    }

}
