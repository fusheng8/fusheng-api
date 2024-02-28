package com.fusheng.api_backend.service.impl;

import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.fusheng.api_backend.config.AlipayTemplate;
import com.fusheng.api_backend.mapper.BalanceOrderMapper;
import com.fusheng.api_backend.service.BalanceOrderServie;
import com.fusheng.common.model.entity.BalanceOrder;
import com.fusheng.common.model.entity.Withdraw;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@Slf4j
public class BalanceOrderServiceImpl implements BalanceOrderServie {

    @Resource
    private AlipayTemplate alipayTemplate;
    @Resource
    private BalanceOrderMapper balanceOrderMapper;

    @Override
    public String getPayPage(BalanceOrder balanceOrder, String returnUrl) {
        //积分转换为人民币
        BigDecimal money = new BigDecimal(balanceOrder.getAmount())
                .setScale(2, RoundingMode.CEILING)
                .divide(new BigDecimal(100), RoundingMode.CEILING);

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(String.valueOf(balanceOrder.getId()));
        model.setTotalAmount(money.toString());
        model.setSubject("充值积分：" + balanceOrder.getAmount() + "个");
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        AlipayTradePagePayResponse response = alipayTemplate.getPayPage(model, returnUrl);

        return response.getBody();
    }

    @Override
    public long payBalance(String account, long userid) {
        BalanceOrder balanceOrder = new BalanceOrder();
        balanceOrder.setAmount(account);
        balanceOrder.setName("充值积分");
        balanceOrder.setUserId(userid);
        balanceOrder.setState(0);
        balanceOrderMapper.insert(balanceOrder);
        return balanceOrder.getId();
    }

    @Override
    public BalanceOrder getById(long orderId) {
        return balanceOrderMapper.selectById(orderId);
    }

    @Override
    public void updateById(BalanceOrder balanceOrder) {
        balanceOrder.setUpdateTime(LocalDateTime.now());
        balanceOrderMapper.updateById(balanceOrder);
    }

    @Override
    public void insertWithdrawOrder(Withdraw withdraw) {
        BalanceOrder balanceOrder = new BalanceOrder();
        balanceOrder.setPayTime(LocalDateTime.now());
        balanceOrder.setAmount(withdraw.getAmount());
        balanceOrder.setName("提现积分");
        balanceOrder.setUserId(1L);
        balanceOrderMapper.insert(balanceOrder);
    }
}
