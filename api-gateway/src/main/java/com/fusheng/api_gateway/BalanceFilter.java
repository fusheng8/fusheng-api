package com.fusheng.api_gateway;

import com.fusheng.GatewayService;
import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.RequestLogs;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Component
public class BalanceFilter implements GlobalFilter, Ordered {
    /**
     * 过期时间
     */
    public static final Long EXPIRE_TIME = 10 * 60 * 1000L;
    @DubboReference
    private GatewayService gatewayService;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 认证失败
     *
     * @param response
     * @return
     */
    private static Mono<Void> authenticateFailed(ServerHttpResponse response, String errMsg) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.bufferFactory().wrap(errMsg.getBytes());
        log.error(errMsg);
        return response.setComplete();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpHeaders headers = request.getHeaders();
        String path = request.getPath().toString();
        String accessKey = headers.getFirst("AccessKey");


        SysUser user = gatewayService.getUserByAccessKey(accessKey);
        ApiInfo apiInfo = gatewayService.getApiInfoByMappingUrl(path);

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                HttpStatusCode statusCode = getStatusCode();
                //如果响应码是200，那么就扣积分
                //记录日志
                RequestLogs requestLogs = new RequestLogs();
                requestLogs.setRequestBody(request.getBody().toString());
                requestLogs.setRequestHeaders(headers.toString());
                requestLogs.setRequestMethod(request.getMethod().toString());
                requestLogs.setRequestUrl(request.getURI().toString());
                requestLogs.setResponseCode(statusCode.value());
                gatewayService.saveRequestLogs(requestLogs);

                if (statusCode == HttpStatus.OK) {
                    //如果是自己调用自己的接口，积分不变
                    if (!Objects.equals(user.getId(), apiInfo.getUserId()) && !"0".equals(apiInfo.getReduceBalance()) && !gatewayService.changeUserBalance(user.getId(), apiInfo).getKey()) {
                        //扣除积分失败
                        return authenticateFailed(response, "扣除积分失败");
                    }
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());

    }


    @Override
    public int getOrder() {
        return -1;
    }

}