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
    public final String fromName = "OnlineJudge";
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void registerEmail(String from, String to, String captcha) {
        try {
            // 创建一个邮件消息
            MimeMessage message = javaMailSender.createMimeMessage();
            // 创建 MimeMessageHelper
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            // 发件人邮箱和名称
            helper.setFrom(from, fromName);
            // 收件人邮箱
            helper.setTo(to);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    public void testEmail(String from, String to) {
        try {
            // 创建一个邮件消息
            MimeMessage message = javaMailSender.createMimeMessage();
            // 创建 MimeMessageHelper
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            // 发件人邮箱和名称
            helper.setFrom(from, fromName);
            // 收件人邮箱
            helper.setTo(to);
            // 邮件标题
            helper.setSubject("Test");
            // 创建一个Context对象
            Context context = new Context();
            //emailTemplate是你要发送的模板我这里用的是Thymeleaf
            String process = "<h1>Test</h1>";
            // 邮件内容
            helper.setText(process, true);
            // 发送
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
