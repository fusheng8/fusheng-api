package com.fusheng.api_gateway;

import cn.hutool.core.collection.ListUtil;
import com.fusheng.AuthorizeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private AuthorizeService authorizeService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        HttpHeaders headers = request.getHeaders();
        String authorization = headers.getFirst("Authorization");
        String accessKey = headers.getFirst("AccessKey");
        String timestamp = headers.getFirst("Timestamp");
        String sign = headers.getFirst("Sign");
        String nonce = headers.getFirst("nonce");

        log.info("结果{}",authorizeService.sayHello("fusheng"));

        // ToDO: 计算签名判断是否放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}