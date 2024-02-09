package com.fusheng.api_gateway;

import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.fusheng.AuthorizeService;
import com.fusheng.common.constant.RedisName;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private AuthorizeService authorizeService;
    @Resource
    private RedissonClient redissonClient;
    /**
     * 过期时间
     */
    public static final Long EXPIRE_TIME = 10 * 60 * 1000L;

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

        if (StringUtils.isAnyBlank(authorization, accessKey, timestamp, sign, nonce)) {
            return authenticateFailed(response);
        }

        // 判断是否有权限
        SysUser user = authorizeService.getUserByAccessKey(accessKey);
        if (user == null) {
            return authenticateFailed(response);
        }
        // 验证时间戳 10s内有效
        if (DateTime.now().getTime() - Long.parseLong(timestamp) > EXPIRE_TIME) {
            return authenticateFailed(response);
        }

        // 验证nonce
        RBucket<String> bucket = redissonClient.getBucket(RedisName.NONCE + accessKey + ":" + nonce);
        if (bucket.isExists()) {
            return authenticateFailed(response);
        } else {
            // 保存nonce
            bucket.set(nonce, EXPIRE_TIME, TimeUnit.MILLISECONDS);
        }
        if (getSign(accessKey, timestamp, nonce).equals(sign)) {
            return authenticateFailed(response);
        }

        // ToDo 判断请求结果决定是否扣除积分
        return chain.filter(exchange);
    }

    /**
     * 认证失败
     *
     * @param response
     * @return
     */
    private static Mono<Void> authenticateFailed(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }


    /**
     * 计算签名
     */
    private String getSign(String accessKey, String timestamp, String nonce) {
        Digester sha1 = new Digester(DigestAlgorithm.SHA1);
        return sha1.digestHex(accessKey + timestamp + nonce);
    }
}