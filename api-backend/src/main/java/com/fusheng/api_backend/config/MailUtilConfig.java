package com.fusheng.api_backend.config;

import com.fusheng.api_backend.utils.MyMailUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailUtilConfig {
    @Bean
    public MyMailUtil addMailUtil() {
        return new MyMailUtil();
    }
}
