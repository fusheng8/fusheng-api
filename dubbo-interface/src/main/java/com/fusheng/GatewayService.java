package com.fusheng;

import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.KVPair;
import com.fusheng.common.model.entity.RequestLogs;
import com.fusheng.common.model.entity.SysUser;

import java.util.List;

public interface GatewayService {

    /**
     * 根据accessKey获取用户信息
     *
     * @param accessKey
     * @return
     */
    SysUser getUserByAccessKey(String accessKey);

    /**
     * 根据mappingUrl获取api信息
     *
     * @param mappingUrl
     * @return
     */
    ApiInfo getApiInfoByMappingUrl(String mappingUrl);

    /**
     * 获取所有api信息
     *
     * @return
     */
    List<ApiInfo> getAllApiInfo();

    /**
     * 改变用户余额
     *
     * @param userId
     * @param apiInfo
     * @return boolean 是否扣除成功
     */
    KVPair<Boolean, String> changeUserBalance(long userId, ApiInfo apiInfo);

    /**
     * 保存请求日志
     *
     * @param requestLogs
     */
    void saveRequestLogs(RequestLogs requestLogs);
}