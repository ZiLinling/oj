package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Contest;
import com.xmut.onlinejudge.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/contest")
public class ContestController {

    @Autowired
    private ContestService contestService;

    /**
     * 添加。
     *
     * @param contest
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Contest contest) {
        return contestService.save(contest);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return contestService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param contest
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Contest contest) {
        return contestService.updateById(contest);
    }


    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public Contest getInfo(@PathVariable Serializable id) {
        return contestService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("list")
    public Result<Page<Row>> page(Integer page, Integer limit, Integer status) {
        Result<Page<Row>> result = new Result<>();
        Page<Row> contestPage = contestService.page(page, limit, status);
        result.success(contestPage, "查询成功");
        return result;
    }
}
