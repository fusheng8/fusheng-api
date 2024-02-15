package com.fusheng.api_backend;

import com.fusheng.api_backend.utils.MyMailUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = BackendApplication.class)
@Slf4j
class BackendApplicationTest {
    @Resource
    MyMailUtil myMailUtil;
    @Test
    void testMail() {
        myMailUtil.sendRegisterCodeMail("2757741589@qq.com","123456");
    }
}