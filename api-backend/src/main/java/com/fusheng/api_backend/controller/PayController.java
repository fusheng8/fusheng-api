package com.fusheng.api_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.fusheng.api_backend.config.AlipayTemplate;
import com.fusheng.api_backend.service.BalanceOrderServie;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.common.model.entity.BalanceOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@Tag(name = "支付相关")
@RequestMapping("/pay")
@RestController
@SaCheckLogin
public class PayController {

    @Resource
    private BalanceOrderServie balanceOrderServie;
    @Resource
    private SysUserService sysUserService;
    @Resource
    private AlipayTemplate alipayTemplate;


    @Operation(summary = "积分充值支付宝支付回调")
    @PostMapping("/notify/balance")
    @SaIgnore
    public void balanceNotify(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        boolean verified = alipayTemplate.notifySignVerified(parameterMap);
        if (!verified) {
            return;
        }
        String tradeStatuses = parameterMap.get("trade_status")[0];
        String outTradeNo = parameterMap.get("out_trade_no")[0];
        if (!"TRADE_SUCCESS".equals(tradeStatuses)) {
            return;
        }
        BalanceOrder balanceOrder = balanceOrderServie.getById(Long.parseLong(outTradeNo));
        sysUserService.deductUserBalance(balanceOrder.getUserId(), true, balanceOrder.getAmount());
        balanceOrder.setUpdateTime(LocalDateTime.now());
        balanceOrder.setPayTime(LocalDateTime.now());
        balanceOrder.setState(1);
        balanceOrderServie.updateById(balanceOrder);
    }
}
