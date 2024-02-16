package com.fusheng.api_backend.service;

/**
 * 验证码服务
 */
public interface MailService {
    /**
     * 发送注册验证码
     *
     * @param to
     */
    void sendRegisterCode(String to);

    /**
     * 发送重置sk验证码
     *
     * @param email
     */
    void sendResetSkCode(String email);
}
