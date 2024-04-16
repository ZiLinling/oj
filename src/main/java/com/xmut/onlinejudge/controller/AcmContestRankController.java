package com.xmut.onlinejudge.controller;

import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.entity.AcmContestRank;
import com.xmut.onlinejudge.service.AcmContestRankService;
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
@RequestMapping("/acmContestRank")
public class AcmContestRankController {

    @Autowired
    private AcmContestRankService acmContestRankService;

    /**
     * 添加。
     *
     * @param acmContestRank
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody AcmContestRank acmContestRank) {


        return acmContestRankService.save(acmContestRank);

    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return acmContestRankService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param acmContestRank
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody AcmContestRank acmContestRank) {
        return acmContestRankService.updateById(acmContestRank);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<AcmContestRank> list() {
        return acmContestRankService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public AcmContestRank getInfo(@PathVariable Serializable id) {
        return acmContestRankService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<AcmContestRank> page(Page<AcmContestRank> page) {
        return acmContestRankService.page(page);
    }

}
