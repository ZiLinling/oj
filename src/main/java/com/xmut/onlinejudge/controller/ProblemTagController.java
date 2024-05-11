package com.xmut.onlinejudge.controller;

import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.ProblemTag;
import com.xmut.onlinejudge.service.ProblemTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 控制层。
 *
 * @author Zi
 * @since 2024-03-05
 */
@RestController
@RequestMapping("/tag")
public class ProblemTagController {

    @Autowired
    private ProblemTagService problemTagService;

    @GetMapping("list")
    public Result<List<ProblemTag>> list() {
        Result<List<ProblemTag>> result = new Result<>();
        result.success(problemTagService.list(), "查询成功");
        return result;
    }

    @GetMapping("")
    public Result<List<ProblemTag>> getList(String keyword) {
        Result<List<ProblemTag>> result = new Result<>();
        result.success(problemTagService.getListByKeyword(keyword), "查询成功");
        return result;
    }


}
