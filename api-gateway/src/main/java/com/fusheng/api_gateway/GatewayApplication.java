package com.fusheng.api_gateway;

import com.fusheng.api_gateway.config.BloomFilterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDubbo
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
//        new BloomFilterConfig().initBloomFilter();
    }
}
