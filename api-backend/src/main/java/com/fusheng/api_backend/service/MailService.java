package com.fusheng.api_backend.service;

/**
 * 验证码服务
 */
public interface MailService {
    /**
     * 发送注册验证码
     *
     * @param email
     */
    void sendRegisterCode(String email);

    /**
     * 发送重置sk验证码
     *
     * @param email
     */
    void sendResetSkCode(String email);

    /**
     * 发送邮箱登录验证码
     */
    void sendEmailLoginCode(String email);

    /**
     * 发送重置密码验证码
     *
     * @param email
     */
    void sendResetPasswordCode(String email);

    /**
     * 发送积分不足题目
     *
     * @param email
     */
    void sendBalanceNotice(String email);
}
