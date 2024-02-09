package com.fusheng;

public interface AuthorizeService {
    /**
     * 检测是否有权限
     * @param accessKey
     * @param timestamp
     * @param sign
     * @param nonce
     * @return
     */
    boolean authorize(String accessKey, String timestamp, String sign, String nonce);

}