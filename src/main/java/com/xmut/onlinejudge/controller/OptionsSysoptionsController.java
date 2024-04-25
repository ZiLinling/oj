package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.entity.OptionsSysoptions;
import com.xmut.onlinejudge.service.OptionsSysoptionsService;
import com.xmut.onlinejudge.utils.JudgeUtil;
import com.xmut.onlinejudge.utils.RedisUtil;
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
@RequestMapping("/config")
public class OptionsSysoptionsController {

    @Autowired
    private OptionsSysoptionsService optionsSysoptionsService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JudgeUtil judgeUtil;

    /**
     * 添加。
     *
     * @param optionsSysoptions
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody OptionsSysoptions optionsSysoptions) {
        return optionsSysoptionsService.save(optionsSysoptions);
    }

    /**
     * 根据主键删除。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return optionsSysoptionsService.removeById(id);
    }

    /**
     * 根据主键更新。
     *
     * @param optionsSysoptions
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody OptionsSysoptions optionsSysoptions) {
        return optionsSysoptionsService.updateById(optionsSysoptions);
    }

    /**
     * 查询所有。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<OptionsSysoptions> list() {
        return optionsSysoptionsService.list();
    }

    /**
     * 根据主键获取详细信息。
     *
     * @param id 主键
     * @return 详情
     */
    @GetMapping("getInfo/{id}")
    public OptionsSysoptions getInfo(@PathVariable Serializable id) {
        return optionsSysoptionsService.getById(id);
    }

    /**
     * 分页查询。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<OptionsSysoptions> page(Page<OptionsSysoptions> page) {
        return optionsSysoptionsService.page(page);
    }

    @GetMapping("languages")
    public Result<JSONObject> getLanguages() {
        Result<JSONObject> result = new Result<>();
        JSONObject data = new JSONObject();
        if (redisUtil.hasKey("languages")) {
            data.put("languages", redisUtil.get("languages"));
        } else {
            OptionsSysoptions languages = optionsSysoptionsService.getValue("languages");
            redisUtil.set("languages", languages.getValue(), 60 * 60);
            data.put("languages", languages.getValue());
        }
        judgeUtil.getLanguageConfig("C");
        result.success(data, "查询成功");
        return result;
    }

    @GetMapping("website")
    public Result<JSONObject> getWebsite() {
        Result<JSONObject> result = new Result<>();
        JSONObject data = new JSONObject();
        if (redisUtil.hasKey("website")) {
            data = (JSONObject) redisUtil.get("website");
        } else {
            // 获取网站配置
            OptionsSysoptions website_base_url = optionsSysoptionsService.getValue("website_base_url");
            OptionsSysoptions website_footer = optionsSysoptionsService.getValue("website_footer");
            OptionsSysoptions website_name = optionsSysoptionsService.getValue("website_name");
            OptionsSysoptions website_name_shortcut = optionsSysoptionsService.getValue("website_name_shortcut");
            OptionsSysoptions allow_register = optionsSysoptionsService.getValue("allow_register");
            OptionsSysoptions submission_list_show_all = optionsSysoptionsService.getValue("submission_list_show_all");
            // 将数据添加到Map中
            data.put("website_base_url", website_base_url.getValue());
            data.put("website_name", website_name.getValue());
            data.put("website_name_shortcut", website_name_shortcut.getValue());
            data.put("website_footer", website_footer.getValue());
            data.put("allow_register", allow_register.getValue());
            data.put("submission_list_show_all", submission_list_show_all.getValue());

            // 将数据存入redis
            redisUtil.set("website", data, 60 * 60);
        }
        result.success(data, "查询成功");
        return result;
    }
}
