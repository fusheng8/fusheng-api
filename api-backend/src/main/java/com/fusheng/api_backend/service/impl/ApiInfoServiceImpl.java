package com.fusheng.api_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusheng.api_backend.mapper.ApiInfoMapper;
import com.fusheng.api_backend.model.entity.ApiInfo;
import com.fusheng.api_backend.service.ApiInfoService;
import org.springframework.stereotype.Service;

@Service
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements ApiInfoService {
}
