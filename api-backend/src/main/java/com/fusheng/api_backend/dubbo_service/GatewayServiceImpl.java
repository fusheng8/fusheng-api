package com.fusheng.api_backend.dubbo_service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fusheng.GatewayService;
import com.fusheng.api_backend.mapper.ApiInfoMapper;
import com.fusheng.api_backend.mapper.SysUserMapper;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RedissonClient;

import java.math.BigInteger;

@DubboService
public class GatewayServiceImpl implements GatewayService {
    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysUserService userService;
    @Resource
    private ApiInfoMapper apiInfoMapper;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 根据accessKey获取用户信息
     *
     * @param accessKey
     * @return
     */
    @Override
    public SysUser getUserByAccessKey(String accessKey) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("access_key", accessKey);
        return userMapper.selectOne(queryWrapper);
    }

    /**
     * 根据apiUrl获取api信息
     *
     * @param apiUrl
     * @return
     */
    @Override
    public ApiInfo getApiInfoByApiUrl(String apiUrl) {
        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<ApiInfo>().eq("url", apiUrl);
        return apiInfoMapper.selectOne(queryWrapper);
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
        return userService.deductUserBalance(userId,false, amount);
    }


}
