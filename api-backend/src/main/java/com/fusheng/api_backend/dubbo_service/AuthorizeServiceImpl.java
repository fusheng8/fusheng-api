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

    @Resource
    private RedissonClient redissonClient;

    public static final Long EXPIRE_TIME = 10 * 60 * 1000L;

    /**
     * 检测是否有权限
     *
     * @param accessKey
     * @param timestamp
     * @param sign
     * @param nonce
     * @return
     */
    @Override
    public boolean authorize(String accessKey, String timestamp, String sign, String nonce) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("access_key", accessKey);
        SysUser user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            return false;
        }
        // 验证时间戳 10s内有效
        if (DateTime.now().getTime() - Long.parseLong(timestamp) > EXPIRE_TIME) {
            return false;
        }

        // 验证nonce
        RBucket<String> bucket = redissonClient.getBucket(RedisName.NONCE + accessKey + ":" + nonce);
        if (bucket.isExists()) {
            return false;
        } else {
            // 保存nonce
            bucket.set(nonce, EXPIRE_TIME, TimeUnit.MILLISECONDS);
        }
        return getSign(accessKey, timestamp, nonce).equals(sign);
    }

    /**
     * 计算签名
     */
    private String getSign(String accessKey, String timestamp, String nonce) {
        Digester sha1 = new Digester(DigestAlgorithm.SHA1);
        return sha1.digestHex(accessKey + timestamp + nonce);
    }
}
