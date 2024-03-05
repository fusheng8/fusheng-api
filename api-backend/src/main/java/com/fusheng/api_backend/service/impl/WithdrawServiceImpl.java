package com.fusheng.api_backend.service.impl;

import com.fusheng.api_backend.mapper.WithdrawMapper;
import com.fusheng.api_backend.service.WithdrawService;
import com.fusheng.common.model.entity.Withdraw;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Resource
    private WithdrawMapper withdrawMapper;

    @Override
    public void withdrawBalance(Withdraw withdraw) {
        withdrawMapper.insert(withdraw);
    }
}
