package com.fusheng.api_client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FushengApiClient {
    // 请求地址
    private static final String url = "http://43.136.38.19:5756/api/wl/yan/yiyan";
    private String accessKey;
    private String secretKey;

    public String send() {
        // 1. 获取时间戳
        String timestamp = String.valueOf(System.currentTimeMillis());
        // 2. 获取随机数
        String nonce = String.valueOf(RandomUtil.randomInt(1000, 10000));
        // 3. 计算签名
        String sign = getSign(accessKey, secretKey, timestamp, nonce);
        // 4. 设置请求
        System.out.println("accessKey = " + accessKey);
        System.out.println("secretKey = " + secretKey);
        System.out.println("timestamp = " + timestamp);
        System.out.println("nonce = " + nonce);
        System.out.println("sign = " + sign);
        try (HttpResponse response = HttpRequest.get(url)
                .header("AccessKey", accessKey)
                .header("Timestamp", timestamp)
                .header("Sign", sign)
                .header("Nonce", nonce)
                .execute()) {
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
