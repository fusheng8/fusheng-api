package com.fusheng.api_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusheng.api_backend.mapper.ApiInfoMapper;
import com.fusheng.api_backend.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.api_backend.model.dto.SysRole.SysRolePageQueryDTO;
import com.fusheng.api_backend.model.entity.ApiInfo;
import com.fusheng.api_backend.model.entity.SysRole;
import com.fusheng.api_backend.model.enums.OrderEnum;
import com.fusheng.api_backend.model.vo.ApiInfo.ApiInfoPageQueryVO;
import com.fusheng.api_backend.service.ApiInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ApiInfoServiceImpl extends ServiceImpl<ApiInfoMapper, ApiInfo> implements ApiInfoService {
    @Resource
    private ApiInfoMapper apiInfoMapper;
    @Override
    public Page<ApiInfo> pageQuery(ApiInfoPageQueryDTO apiInfoPageQueryDTO) {
        Page<ApiInfo> queryPage = new Page<>(apiInfoPageQueryDTO.getCurrent(), apiInfoPageQueryDTO.getPageSize());
        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(apiInfoPageQueryDTO.getName())) {
            queryWrapper.like("name", apiInfoPageQueryDTO.getName());
        }
        if (apiInfoPageQueryDTO.getStatus()!=null) {
            queryWrapper.eq("status", apiInfoPageQueryDTO.getStatus());
        }
        if (apiInfoPageQueryDTO.getMethod()!=null) {
            queryWrapper.eq("method", apiInfoPageQueryDTO.getMethod());
        }
        if (apiInfoPageQueryDTO.getOrder()!=null&&StringUtils.isNotBlank(apiInfoPageQueryDTO.getColumn())) {
            switch (apiInfoPageQueryDTO.getOrder()) {
                case asc:
                    queryWrapper.orderByAsc(apiInfoPageQueryDTO.getColumn());
                    break;
                case desc:
                    queryWrapper.orderByDesc(apiInfoPageQueryDTO.getColumn());
                    break;
            }
        }
        return apiInfoMapper.selectPage(queryPage, queryWrapper);
    }
}
