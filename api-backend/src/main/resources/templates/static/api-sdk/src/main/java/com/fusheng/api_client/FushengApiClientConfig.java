package com.fusheng.api_client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fusheng.api.client")
@Data
@ComponentScan
public class FushengApiClientConfig {

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    @Bean
    public FushengApiClient yuApiClient() {
        return new FushengApiClient(accessKey, secretKey);
    }
}