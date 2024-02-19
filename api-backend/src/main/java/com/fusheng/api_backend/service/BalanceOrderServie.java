package com.fusheng.api_backend.service;

import com.fusheng.common.model.entity.BalanceOrder;
import com.fusheng.common.model.entity.Withdraw;

public interface BalanceOrderServie {
    /**
     * 用户充值积分
     *
     * @param count
     * @return
     */
    String getPayPage(BalanceOrder count, String returnUrl);

    /**
     * 创建充值订单
     *
     * @param account
     * @param userid
     * @return
     */
    long payBalance(String account, long userid);

    /**
     * 根据id获取订单
     *
     * @param orderId
     * @return
     */
    BalanceOrder getById(long orderId);

    /**
     * 更新订单
     *
     * @param balanceOrder
     */
    void updateById(BalanceOrder balanceOrder);

    /**
     * 提现
     *
     * @param withdraw
     */
    void insertWithdrawOrder(Withdraw withdraw);

}
