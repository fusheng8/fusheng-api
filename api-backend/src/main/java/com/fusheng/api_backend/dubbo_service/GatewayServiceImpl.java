package com.fusheng.api_backend.dubbo_service;

import com.fusheng.GatewayService;
import com.fusheng.api_backend.service.ApiInfoService;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class GatewayServiceImpl implements GatewayService {
    @Resource
    private SysUserService userService;
    @Resource
    private ApiInfoService apiInfoService;

    /**
     * 根据accessKey获取用户信息
     *
     * @param accessKey
     * @return
     */
    @Override
    public SysUser getUserByAccessKey(String accessKey) {
        //如果缓存中没有，就从数据库中查找
        SysUser user = userService.getByAccessKey(accessKey);
        return user;
    }

    /**
     * 根据apiUrl获取api信息
     *
     * @param apiUrl
     * @return
     */
    @Override
    public ApiInfo getApiInfoByApiUrl(String apiUrl) {
        return apiInfoService.getByUrl(apiUrl);
    }

    /**
     * 扣除用户余额
     *
     * @param userId
     * @param amount
     * @return boolean 是否扣除成功
     */
    @Override
    public boolean deductUserBalance(long userId, String amount) {
        return userService.deductUserBalance(userId, false, amount);
    }


}
