package com.fusheng;

import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.SysUser;
import javafx.util.Pair;

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
     * 改变用户余额
     *
     * @param userId
     * @param apiInfo
     * @return boolean 是否扣除成功
     */
    Pair<Boolean,String> changeUserBalance(long userId, ApiInfo apiInfo);

}