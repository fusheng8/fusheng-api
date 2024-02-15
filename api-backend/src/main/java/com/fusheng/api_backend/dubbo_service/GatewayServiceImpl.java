package com.fusheng.api_backend.dubbo_service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fusheng.GatewayService;
import com.fusheng.api_backend.mapper.ApiInfoMapper;
import com.fusheng.api_backend.mapper.SysUserMapper;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.common.constant.RedisName;
import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

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
        //因为ak永远不会变化，所以这里绑定ak对应的id，再通过id查找用户信息，这样修改用户信息不需要修改这里，便于信息维护
        RBucket<Long> bucket = redissonClient.getBucket(RedisName.ACCESS_KEY_USER_ID + accessKey);
        if (bucket.isExists()) {
            return userService.getById(bucket.get());
        }
        //如果缓存中没有，就从数据库中查找
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("access_key", accessKey);
        SysUser user = userMapper.selectOne(queryWrapper);
        bucket.set(user.getId());
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
        return userService.deductUserBalance(userId, false, amount);
    }


}
