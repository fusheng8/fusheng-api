package com.fusheng.common.constant;

/**
 * redis key 常量
 */
public class RedisName {
    /**
     * 公共前缀
     */
    public static final String COMMON_PREFIX = "fusheng_api:";
    /**
     * 接口防重放的nonce存储
     */
    public static final String NONCE = COMMON_PREFIX + "nonce:";
    /**
     * 修改用户余额的锁
     */
    public static final String USER_BALANCE_LOCK = COMMON_PREFIX + "user_balance_lock:";
}
