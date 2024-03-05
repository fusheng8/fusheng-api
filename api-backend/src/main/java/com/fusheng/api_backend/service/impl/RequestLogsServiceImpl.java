package com.fusheng.api_backend.service.impl;

import com.fusheng.api_backend.mapper.RequestLogsMapper;
import com.fusheng.api_backend.service.RequestLogsService;
import com.fusheng.common.model.entity.RequestLogs;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RequestLogsServiceImpl implements RequestLogsService {
    @Resource
    private RequestLogsMapper requestLogsMapper;

    @Override
    @Async
    public void save(RequestLogs requestLogs) {
        requestLogsMapper.insert(requestLogs);

    }
}
