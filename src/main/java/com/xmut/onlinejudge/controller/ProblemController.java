package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Row;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.Problem;
import com.xmut.onlinejudge.service.ProblemService;
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
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    /**
     * 添加。
     *
     * @param problem
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody Problem problem) {
        return problemService.save(problem);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return problemService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param problem
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody Problem problem) {
        return problemService.updateById(problem);
    }


    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public Problem getInfo(@PathVariable Serializable id) {
        return problemService.getById(id);
    }


    @GetMapping("list")
    public Result<Page<Row>> page(Integer limit, Integer page, String keyword, String difficulty, String tag) {
        Result<Page<Row>> result = new Result<>();
        //tag功能未实现
        Page<Row> problemPage = problemService.page(page, limit, keyword, difficulty, tag);
        result.success(problemPage, "查询成功");
        return result;
    }

}
