package com.fusheng.api_backend.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.service.CodeService;
import com.fusheng.common.constant.RedisName;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CodeServiceImpl implements CodeService {
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private JavaMailSender mailSender;
    // 配置文件中我的qq邮箱
    @Value("${spring.mail.username}")
    private String from;
    //注册验证码时效
    private static final long REGISTER_CODE_EXPIRE = 5L;

    /**
     * 发送注册验证码
     *
     * @param to
     */
    @Override
    public void sendRegisterCode(String to) {
        RBucket<String> bucket = redissonClient.getBucket(RedisName.CODE_REGISTER + to);
        //检测是否频繁
        if (bucket.isExists()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送验证码过于频繁，请稍后再试。");
        }
        String code = RandomUtil.randomNumbers(6);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("注册验证码");
        message.setText("您的注册验证码为：" + code + "，请在" + REGISTER_CODE_EXPIRE + "分钟内完成注册。");
        mailSender.send(message);
        //将验证码存入redis
        bucket.set(code, REGISTER_CODE_EXPIRE, TimeUnit.MINUTES);
        //返回验证码凭证
    }
}
