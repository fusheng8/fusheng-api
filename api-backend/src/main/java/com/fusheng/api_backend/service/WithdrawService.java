package com.fusheng.api_backend.service;

import com.fusheng.common.model.entity.Withdraw;

public interface WithdrawService {

    /**
     * 提现
     *
     * @param withdraw
     */
    void withdrawBalance(Withdraw withdraw);
}
