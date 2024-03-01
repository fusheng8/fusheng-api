package com.fusheng.api_gateway;

import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.fusheng.GatewayService;
import com.fusheng.common.constant.RedisKey;
import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.RequestLogs;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.redisson.api.RBucket;
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

import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
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
        String path = request.getPath().toString();

        // 判断是否存在该接口
        ApiInfo apiInfo = gatewayService.getApiInfoByMappingUrl(path);
        if (apiInfo == null) {
            return authenticateFailed(response, "接口不存在");
        }

        //判断接口状态是否是已上线
        if (apiInfo.getStatus() != 3) {
            return authenticateFailed(response, "接口状态异常");
        }

        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("AccessKey");
        String timestamp = headers.getFirst("Timestamp");
        String sign = headers.getFirst("Sign");
        String nonce = headers.getFirst("nonce");

        if (StringUtils.isAnyBlank(accessKey, timestamp, sign, nonce)) {
            return authenticateFailed(response, "参数不完整");
        }

        // 判断是否有权限
        SysUser user = gatewayService.getUserByAccessKey(accessKey);
        if (user != null && !authenticate(accessKey, user.getSecretKey(), timestamp, nonce, sign)) {
            return authenticateFailed(response, "认证失败");
        }


        BigInteger bigInteger1 = new BigInteger(user.getBalance());
        BigInteger bigInteger2 = new BigInteger(apiInfo.getReduceBalance());

        //判断是否积分足够（初次检测，先过滤一部分）
        if (!"0".equals(apiInfo.getReduceBalance()) &&
                bigInteger1.compareTo(bigInteger2) < 0) {
            return authenticateFailed(response, "积分不足");
        }

        HttpStatusCode statusCode = response.getStatusCode();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

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
                    if (!Objects.equals(user.getId(), apiInfo.getUserId()) &&!"0".equals(apiInfo.getReduceBalance()) && !gatewayService.changeUserBalance(user.getId(), apiInfo).getKey()) {
                        //扣除积分失败
                        return authenticateFailed(response, "扣除积分失败");
                    }
                }

                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(decoratedResponse).build());

    }

    /**
     * 验证是否有权限
     */
    private boolean authenticate(String accessKey, String secretKey, String timestamp, String nonce, String sign) {


        // 验证时间戳 10s内有效
        if (DateTime.now().getTime() - Long.parseLong(timestamp) > EXPIRE_TIME) {
            return false;
        }

        // 验证nonce
        RBucket<String> bucket = redissonClient.getBucket(RedisKey.API_INFO_NONCE + accessKey + ":" + nonce);
        if (bucket.isExists()) {
            return false;
        } else {
            // 保存nonce
            bucket.set(nonce, EXPIRE_TIME, TimeUnit.MILLISECONDS);
        }
        if (!getSign(accessKey, secretKey, timestamp, nonce).equals(sign)) {
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return -1;
    }


    /**
     * 计算签名
     */
    private String getSign(String accessKey, String secretKey, String timestamp, String nonce) {
        Digester sha1 = new Digester(DigestAlgorithm.SHA1);
        String s = sha1.digestHex(timestamp + accessKey + secretKey + nonce);
        return sha1.digestHex(timestamp + accessKey + secretKey + nonce);
    }
}