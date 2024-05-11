package com.xmut.onlinejudge.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xmut.onlinejudge.base.Result;
import com.xmut.onlinejudge.service.OptionsSysoptionsService;
import com.xmut.onlinejudge.utils.EmailUtil;
import com.xmut.onlinejudge.utils.JudgeUtil;
import com.xmut.onlinejudge.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private EmailUtil emailUtil;


    @GetMapping("languages")
    public Result<JSONObject> getLanguages() {
        Result<JSONObject> result = new Result<>();
        JSONObject data = new JSONObject();
        JSONArray spjLanguages = new JSONArray(); // 用于存放存在 spj 的数据
        if (redisUtil.hasKey("languages") && redisUtil.hasKey("spj_languages")) {
            data.put("languages", redisUtil.get("languages"));
            data.put("spj_languages", redisUtil.get("spj_languages"));
        } else {
            JSONArray languages = (JSONArray) optionsSysoptionsService.getValue("languages");
            for (Object language : languages) {
                JSONObject languageObj = (JSONObject) language;
                if (languageObj.containsKey("spj")) {
                    spjLanguages.add(languageObj);
                }
            }
            redisUtil.set("languages", languages, 60 * 60);
            redisUtil.set("spj_languages", spjLanguages, 60 * 60);
            data.put("languages", languages);
            data.put("spj_languages", spjLanguages);
        }
        result.success(data, "查询成功");
        return result;
    }


    @GetMapping("smtp")
    public Result<JSONObject> getSmtp() {
        Result<JSONObject> result = new Result<>();
        JSONObject smtpConfig;
        if (redisUtil.hasKey("smtp")) {
            smtpConfig = (JSONObject) redisUtil.get("smtp");
        } else {
            smtpConfig = (JSONObject) optionsSysoptionsService.getValue("smtp_config");
            redisUtil.set("smtp", smtpConfig, 60 * 60);
        }
        result.success(smtpConfig, "查询成功");
        return result;
    }

    @PutMapping("smtp")
    public Result<JSONObject> updateSmtp(@RequestBody JSONObject smtpConfig) {
        Result<JSONObject> result = new Result<>();
        optionsSysoptionsService.updateValue("smtp_config", smtpConfig);
        redisUtil.set("smtp", smtpConfig, 60 * 60);
        result.success(null, "更新成功");
        return result;
    }

    @PostMapping("smtp/test")
    public Result<JSONObject> smtpTest(@RequestBody JSONObject data) {
        Result<JSONObject> result = new Result<>();
        String email = data.getString("email");
        JSONObject smtpConfig;
        if (redisUtil.hasKey("smtp")) {
            smtpConfig = (JSONObject) redisUtil.get("smtp");
        } else {
            smtpConfig = (JSONObject) optionsSysoptionsService.getValue("smtp_config");
            redisUtil.set("smtp", smtpConfig, 60 * 60);
        }
        emailUtil.testEmail(smtpConfig.getString("email"), email);
        result.success(null, "已发送测试邮件");
        return result;
    }


    @GetMapping("website")
    public Result<JSONObject> getWebsite() {
        Result<JSONObject> result = new Result<>();
        JSONObject data = new JSONObject();
        if (redisUtil.hasKey("website")) {
            data = (JSONObject) redisUtil.get("website");
        } else {
            // 将数据添加到Map中
            data.put("website_base_url", optionsSysoptionsService.getValue("website_base_url"));
            data.put("website_name", optionsSysoptionsService.getValue("website_name"));
            data.put("website_name_shortcut", optionsSysoptionsService.getValue("website_name_shortcut"));
            data.put("website_footer", optionsSysoptionsService.getValue("website_footer"));
            data.put("allow_register", optionsSysoptionsService.getValue("allow_register"));
            // 将数据存入redis
            redisUtil.set("website", data, 60 * 60);
        }
        result.success(data, "查询成功");
        return result;
    }

    @PostMapping("website")
    public Result<JSONObject> updateWebsite(@RequestBody JSONObject data) {
        Result<JSONObject> result = new Result<>();
        optionsSysoptionsService.updateValue("website_base_url", data.getString("website_base_url"));
        optionsSysoptionsService.updateValue("website_name", data.getString("website_name"));
        optionsSysoptionsService.updateValue("website_name_shortcut", data.getString("website_name_shortcut"));
        optionsSysoptionsService.updateValue("website_footer", data.getString("website_footer"));
        optionsSysoptionsService.updateValue("allow_register", data.getBoolean("allow_register"));
        redisUtil.set("website", data, 60 * 60);
        result.success(null, "更新成功");
        return result;
    }
}
