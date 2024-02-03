package com.fusheng.api_backend.utils;

import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Value;

public class PasswordUtil {
    @Value("${fusheng.encryptKey}")
    private static String encryptKey;

    /**
     * 加密
     *
     * @param password
     * @return
     */
    public static String encrypt(String password) {
        return SecureUtil.md5(encryptKey+ password);
    }
}
