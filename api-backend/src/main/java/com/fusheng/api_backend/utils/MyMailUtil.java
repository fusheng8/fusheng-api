package com.fusheng.api_backend.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 发送邮件工具类 MailUtil
 *
 * @author heshi
 * @date 2021/3/22 16:52
 */


public class MyMailUtil {

    @Resource
    private JavaMailSender mailSender;

    // 配置文件中我的qq邮箱
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送注册验证码
     */
    public void sendRegisterCodeMail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("注册验证码");
        message.setText("您的注册验证码为：" + code + "，请在5分钟内完成注册。");
        mailSender.send(message);
    }
}