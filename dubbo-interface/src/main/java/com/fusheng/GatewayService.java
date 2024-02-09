package com.fusheng;

import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.SysUser;

public interface GatewayService {

    /**
     * 根据accessKey获取用户信息
     *
     * @param accessKey
     * @return
     */
    SysUser getUserByAccessKey(String accessKey);

    /**
     * 根据apiUrl获取api信息
     *
     * @param apiUrl
     * @return
     */
    ApiInfo getApiInfoByApiUrl(String apiUrl);

    /**
     *
     * @param userId
     * @param amount
     * @return boolean 是否扣除成功
     */
    boolean deductUserBalance(long userId, String amount);

}