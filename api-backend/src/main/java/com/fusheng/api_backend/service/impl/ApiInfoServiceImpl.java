package com.fusheng.api_backend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusheng.api_backend.mapper.ApiInfoMapper;
import com.fusheng.api_backend.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.api_backend.model.dto.ApiInfo.ApiInfoSavaOrUpdateDTO;
import com.fusheng.api_backend.model.entity.ApiInfo;
import com.fusheng.api_backend.service.ApiInfoService;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements ApiInfoService {
    @Resource
    private ApiInfoMapper apiInfoMapper;

    @Override
    public Page<ApiInfo> pageQuery(ApiInfoPageQueryDTO dto) {
        Page<ApiInfo> queryPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            queryWrapper.like("name", dto.getName());
        }
        if (dto.getStatus() != null) {
            queryWrapper.eq("status", dto.getStatus());
        }
        if (dto.getMethod() != null) {
            queryWrapper.eq("method", dto.getMethod());
        }
        if (dto.getOrder() != null && StringUtils.isNotBlank(dto.getColumn())) {
            switch (dto.getOrder()) {
                case asc:
                    queryWrapper.orderByAsc(dto.getColumn());
                    break;
                case desc:
                    queryWrapper.orderByDesc(dto.getColumn());
                    break;
            }
        }
        return apiInfoMapper.selectPage(queryPage, queryWrapper);
    }

    @Override
    public boolean saveOrUpdateApiInfo(ApiInfoSavaOrUpdateDTO dto) {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(dto, apiInfo);
        apiInfo.setUserId(StpUtil.getLoginIdAsLong());
        if (dto.getResponseParams() != null)
            apiInfo.setResponseParams(new Gson().toJson(dto.getResponseParams()));
        else apiInfo.setResponseParams(null);

        if (dto.getRequestHeader() != null)
            apiInfo.setRequestHeader(new Gson().toJson(dto.getRequestHeader()));
        else apiInfo.setRequestHeader(null);

        if (dto.getRequestParams() != null)
            apiInfo.setRequestParams(new Gson().toJson(dto.getRequestParams()));
        else apiInfo.setRequestParams(null);

        if (dto.getId() == null) {
            return apiInfoMapper.insert(apiInfo) > 0;
        } else {
            return apiInfoMapper.updateById(apiInfo) > 0;
        }
    }
}
