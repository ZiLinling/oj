package com.xmut.onlinejudge.config;

import com.alibaba.fastjson.JSONObject;
import com.xmut.onlinejudge.service.OptionsSysoptionsService;
import com.xmut.onlinejudge.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Autowired
    private OptionsSysoptionsService optionService;

    @Autowired
    private RedisUtil redisUtil;

    @Bean
    public JavaMailSenderImpl javaMailSender() {
        JSONObject smtpConfig;
        if (redisUtil.hasKey("smtp")) {
            smtpConfig = (JSONObject) redisUtil.get("smtp");
        } else {
            smtpConfig = (JSONObject) optionService.getValue("smtp_config");
            redisUtil.set("smtp", smtpConfig, 60 * 60);
        }
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(smtpConfig.getString("server")); // 邮件服务器主机名或IP地址
        mailSender.setPort(smtpConfig.getIntValue("port")); // 邮件服务器端口号
        mailSender.setUsername(smtpConfig.getString("email")); // 发件人邮箱
        mailSender.setPassword(smtpConfig.getString("password")); // 授权码
        mailSender.setDefaultEncoding("UTF-8");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp"); // 使用SMTP协议
        props.put("mail.smtp.auth", "true"); // 启用SMTP身份验证
        props.put("mail.smtp.starttls.enable", smtpConfig.getBoolean("tls")); // 启用TLS加密
        return mailSender;
    }
}
