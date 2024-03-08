package com.fusheng.api_gateway.config;

import com.fusheng.GatewayService;
import com.fusheng.common.constant.RedisKey;
import com.fusheng.common.model.entity.ApiInfo;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class BloomFilterConfig {
    private static final int BOOM_FILTER_SIZE = 100000;
    private static final double BOOM_FILTER_ERROR_RATE = 0.01;
    @DubboReference
    private GatewayService gatewayService;
    @Resource
    private RedissonClient redissonClient;

    @PostConstruct
    public void initBloomFilter() {

        log.info("初始化布隆过滤器...");
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(RedisKey.API_BLOOM_FILTER);
        bloomFilter.tryInit(BOOM_FILTER_SIZE, BOOM_FILTER_ERROR_RATE);
        List<ApiInfo> apiList = gatewayService.getAllApiInfo();
        for (ApiInfo apiInfo : apiList) {
            bloomFilter.add(apiInfo.getMappingUrl());
        }
        log.info("初始化布隆过滤器完成！");
    }
}
