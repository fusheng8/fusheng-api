package com.fusheng.api_backend.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.service.MailService;
import com.fusheng.common.constant.RedisKey;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MailServiceImpl implements MailService {
    //注册验证码时效
    private static final long REGISTER_CODE_EXPIRE = 5L;
    //重置sk验证码时效
    private static final long RESET_SK_CODE_EXPIRE = 5L;
    //邮箱登录验证码时效
    private static final long EMAIL_LOGIN_CODE_EXPIRE = 5L;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private JavaMailSender mailSender;
    // 配置文件中我的qq邮箱
    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送注册验证码
     *
     * @param email
     */
    @Override
    public void sendRegisterCode(String email) {
        RBucket<String> bucket = redissonClient.getBucket(RedisKey.CODE_REGISTER + email);
        //检测是否频繁
        if (bucket.isExists()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送验证码过于频繁，请稍后再试。");
        }
        String code = RandomUtil.randomNumbers(6);
        sendCodeEmail(email, code, "注册", REGISTER_CODE_EXPIRE);
        //将验证码存入redis
        bucket.set(code, REGISTER_CODE_EXPIRE, TimeUnit.MINUTES);
    }

    @Override
    public void sendResetSkCode(String email) {
        RBucket<String> bucket = redissonClient.getBucket(RedisKey.CODE_RESER_SK + email);
        //检测是否频繁
        if (bucket.isExists()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送验证码过于频繁，请稍后再试。");
        }
        String code = RandomUtil.randomNumbers(6);
        sendCodeEmail(email, code, "重置SecretKey", RESET_SK_CODE_EXPIRE);
        //将验证码存入redis
        bucket.set(code, RESET_SK_CODE_EXPIRE, TimeUnit.MINUTES);
    }

    @Override
    public void sendEmailLoginCode(String email) {
        RBucket<String> bucket = redissonClient.getBucket(RedisKey.CODE_EMAIL_LOGIN + email);
        //检测是否频繁
        if (bucket.isExists()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "发送验证码过于频繁，请稍后再试。");
        }
        String code = RandomUtil.randomNumbers(6);
        sendCodeEmail(email, code, "邮箱登录", EMAIL_LOGIN_CODE_EXPIRE);
        //将验证码存入redis
        bucket.set(code, EMAIL_LOGIN_CODE_EXPIRE, TimeUnit.MINUTES);
    }

    private void sendCodeEmail(String email, String code, String operate, long expire) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject(operate);
        message.setText("您正在进行" + operate + "您的验证码为：" + code + "，请在" + expire + "分钟内完成注册。");
        mailSender.send(message);
    }
}
