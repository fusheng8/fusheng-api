package com.fusheng.api_backend.dubbo_service;

import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fusheng.AuthorizeService;
import com.fusheng.api_backend.mapper.SysUserMapper;
import com.fusheng.common.constant.RedisName;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

@DubboService
public class AuthorizeServiceImpl implements AuthorizeService {
    @Resource
    private SysUserMapper userMapper;

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


}
