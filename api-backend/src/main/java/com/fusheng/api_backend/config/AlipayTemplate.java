package com.fusheng.api_backend.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.fusheng.api_backend.properties.AlipayProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AlipayTemplate {
    @Resource
    AlipayProperties alipayProperties;

    public AlipayClient getClient() {
        return new DefaultAlipayClient(
                alipayProperties.getServerUrl(),
                alipayProperties.getAppId(),
                alipayProperties.getPrivateKey(),
                alipayProperties.getFormat(),
                alipayProperties.getCharset(),
                alipayProperties.getAlipayPublicKey(),
                alipayProperties.getSignType()
        );
    }

    /**
     * 获取支付宝支付页面请求
     *
     * @param model
     * @param returnUrl
     * @return
     */
    public AlipayTradePagePayResponse getPayPage(AlipayTradeWapPayModel model, String returnUrl) {
        AlipayClient alipayClient = getClient();
        AlipayTradePagePayRequest request = getPayRequest(alipayProperties.getNotifyUrl(), returnUrl, model);
        AlipayTradePagePayResponse response = null;
        try {
            response = alipayClient.pageExecute(request, "POST");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 获取支付宝支付请求
     *
     * @param notifyUrl
     * @param returnUrl
     * @param model
     * @return
     */
    private AlipayTradePagePayRequest getPayRequest(String notifyUrl, String returnUrl, AlipayTradeWapPayModel
            model) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);
        request.setBizModel(model);
        return request;
    }

    /**
     * 验证回调签名
     */
    public boolean notifySignVerified(Map<String, String[]> requestParams) {

        Map<String, String> params = new HashMap<>();

        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, alipayProperties.getAlipayPublicKey(),
                    alipayProperties.getCharset(), alipayProperties.getSignType());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return signVerified;
    }
}
