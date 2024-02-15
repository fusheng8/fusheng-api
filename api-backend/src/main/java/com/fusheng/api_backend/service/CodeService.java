package com.fusheng.api_backend.service;

/**
 * 验证码服务
 */
public interface CodeService {
    /**
     * 发送注册验证码
     *
     * @param to
     */
    void sendRegisterCode(String to);
}
