package com.xmut.onlinejudge.config;

import com.xmut.onlinejudge.base.Result;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
public class WebsiteConfig {
    private final Boolean allow_register = true;
    private final Boolean submission_list_show_all = true;
    private final String website_base_url = "http://127.0.0.1";
    private final String website_footer = "test";
    private final String website_name = "厦门理工学院 OnlineJudge";
    private final String website_name_shortcut = "XMUT OJ";

    @GetMapping("website")
    private Result<WebsiteConfig> getWebsite() {
        Result<WebsiteConfig> result = new Result<>();
        result.success(this, "获取网站配置成功");
        return result;
    }

}
