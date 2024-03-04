package com.fusheng.api_client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class FushengApiClient {
    // 请求地址
    private static final String url = "${url}";
    private String accessKey;
    private String secretKey;

    public String send(Map<String, String> headers, Map<String, String> params) {
        // 获取时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        // 获取随机数
        String nonce = String.valueOf(RandomUtil.randomInt(1000, 10000));
        // 计算签名
        String sign = getSign(accessKey, secretKey, timestamp, nonce);
        // 设置请求
        <#if method == "GET">
        HttpRequest request = HttpRequest.get(url)
        </#if>
        <#if method == "POST">
        HttpRequest request = HttpRequest.post(url)
                </#if>
            .header("AccessKey", accessKey)
            .header("Timestamp", timestamp)
            .header("Sign", sign)
            .header("Nonce", nonce);

        // 设置请求信息
        request = request.headerMap(headers, true);
        request = request.formStr(params);


        try (HttpResponse response = request.execute()) {
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 计算签名
     */
    private String getSign(String accessKey, String secretKey, String timestamp, String nonce) {
        Digester sha1 = new Digester(DigestAlgorithm.SHA1);
        return sha1.digestHex(timestamp + accessKey + secretKey + nonce);
    }
}
