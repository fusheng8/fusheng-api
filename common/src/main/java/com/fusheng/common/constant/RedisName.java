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
     * 接口防重放的nonce存储 :ak:nonce
     */
    public static final String API_INFO_NONCE = COMMON_PREFIX + "api_info:" + "nonce:";
    /**
     * 修改用户余额的锁 :用户id
     */
    public static final String USER_BALANCE_LOCK = COMMON_PREFIX + "user:" + "user_balance_lock:";
    /**
     * 根据id查询用户 :用户id
     */
    public static final String USER_BY_ID = COMMON_PREFIX + "user:" + "user_by_id:";
    /**
     * 根据accessKey查询用户 id:accessKey
     */
    public static final String USER_ID_BY_ACCESS_KEY = COMMON_PREFIX + "user:" + "user_id_by_access_key:";
    /**
     * 根据id查询角色 :角色id
     */
    public static final String ROLE_BY_ID = COMMON_PREFIX + "role:" + "role_by_id:";
    /**
     * 根据id查询api信息 :api的id
     */
    public static final String API_INFO_BY_ID = COMMON_PREFIX + "api_info:" + "api_info_by_id:";

    /**
     * 根据url查询api的id :url
     */
    public static final String API_INFO_ID_BY_URL = COMMON_PREFIX + "api_info:" + "api_info_id_by_url:";
    /**
     * 注册验证码: :邮箱
     */
    public static final String CODE_REGISTER = COMMON_PREFIX + "code:" + "code_register:";

}

