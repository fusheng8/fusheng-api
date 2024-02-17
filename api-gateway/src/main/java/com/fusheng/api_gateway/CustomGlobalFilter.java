package com.fusheng.api_gateway;

import cn.hutool.core.date.DateTime;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.fusheng.GatewayService;
import com.fusheng.common.constant.RedisKey;
import com.fusheng.common.model.entity.ApiInfo;
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
import org.springframework.core.io.buffer.DataBufferFactory;
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
        String uri = request.getURI().toString();

        // 判断是否存在该接口
        ApiInfo apiInfo = gatewayService.getApiInfoByApiUrl(uri.split("\\?")[0]);
        if (apiInfo == null) {
            return authenticateFailed(response, "无效的接口");
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


        DataBufferFactory bufferFactory = response.bufferFactory();
        HttpStatusCode statusCode = response.getStatusCode();


        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {

            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                // ToDo 记录日志
//                    if (body instanceof Flux) {
//                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
//                        //
//                        return super.writeWith(fluxBody.map(dataBuffer -> {
//                            byte[] content = new byte[dataBuffer.readableByteCount()];
//                            dataBuffer.read(content);
//                            DataBufferUtils.release(dataBuffer);//释放掉内存
//                            // 构建日志
//                            StringBuilder sb2 = new StringBuilder(200);
//                            sb2.append("<--- {} {} \n");
//                            List<Object> rspArgs = new ArrayList<>();
//                            rspArgs.add(response.getStatusCode());
//                            //rspArgs.add(requestUrl);
//                            String data = new String(content, StandardCharsets.UTF_8);//data
//                            sb2.append(data);
//                            log.info("测试");
//                            log.info(sb2.toString(), rspArgs.toArray());//log.info("<-- {} {}\n", originalResponse.getStatusCode(), data);
//                            return bufferFactory.wrap(content);
//                        }));
//                    } else {
//                        log.error("<--- {} 响应code异常", getStatusCode());
//                    }

                //如果响应码是200，那么就扣积分
                if (statusCode == HttpStatus.OK) {
                    if (!"0".equals(apiInfo.getReduceBalance()) && !gatewayService.deductUserBalance(user.getId(), apiInfo.getReduceBalance())) {
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