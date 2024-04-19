package com.xmut.onlinejudge.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;


    @Async
    public void send(String email, String captcha) throws Exception {

        // 创建一个邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();

        // 创建 MimeMessageHelper
        MimeMessageHelper helper = new MimeMessageHelper(message, false);

        // 发件人邮箱和名称
        helper.setFrom("XMUT_OnlineJudge@163.com", "OnlineJudge");
        // 收件人邮箱
        helper.setTo(email);
        // 邮件标题
        helper.setSubject("欢迎注册 OnlineJudge");
        // 创建一个Context对象
        Context context = new Context();
        //设置传入模板的页面的参数
        context.setVariable("captcha", captcha);
        //emailTemplate是你要发送的模板我这里用的是Thymeleaf
        String process = templateEngine.process("email", context);
        // 邮件内容
        helper.setText(process, true);
        // 发送
        javaMailSender.send(message);
    }
}
