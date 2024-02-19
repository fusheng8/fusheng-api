package com.fusheng.api_backend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.mapper.ApiInfoMapper;
import com.fusheng.api_backend.service.ApiInfoService;
import com.fusheng.common.constant.RedisKey;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoSavaOrUpdateDTO;
import com.fusheng.common.model.entity.ApiInfo;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiInfoServiceImpl implements ApiInfoService {
    @Resource
    private ApiInfoMapper apiInfoMapper;
    @Resource
    private RedissonClient redissonClient;

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
        if (dto.getMethod() != null) {
            queryWrapper.eq("method", dto.getMethod());
        }
        if (dto.getUserId() != null) {
            queryWrapper.eq("user_id", dto.getUserId());
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
    public boolean saveOrUpdate(ApiInfoSavaOrUpdateDTO dto) {
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
            int i = apiInfoMapper.updateById(apiInfo);
            if (i > 0) {
                redissonClient.getBucket(RedisKey.API_INFO_BY_ID + dto.getId()).set(apiInfo);
                return true;
            }
            return false;
        }
    }

    @Override
    public ApiInfo getById(Long id) {
        ApiInfo apiInfo = apiInfoMapper.selectById(id);
        if (apiInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        redissonClient.getBucket(RedisKey.API_INFO_BY_ID + id).set(apiInfo);
        return apiInfo;
    }

    @Override
    public boolean removeByIds(List<Long> ids) {
        int i = apiInfoMapper.deleteBatchIds(ids);
        if (i > 0) {
            ids.forEach(id -> {
                redissonClient.getBucket(RedisKey.API_INFO_BY_ID + id).delete();
            });
            return true;
        }
        return false;
    }

    @Override
    public ApiInfo getByUrl(String apiUrl) {
        RBucket<Long> bucket = redissonClient.getBucket(RedisKey.API_INFO_ID_BY_URL + apiUrl);
        if (bucket.isExists()) {
            return this.getById(bucket.get());
        }
        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", apiUrl);
        ApiInfo apiInfo = apiInfoMapper.selectOne(queryWrapper);
        bucket.set(apiInfo.getId());
        return apiInfo;
    }

    @Override
    public List<ApiInfo> queryByIds(List<Long> ids) {
        return apiInfoMapper.selectBatchIds(ids);
    }

    @Override
    public Boolean reviewApi(Long id, Integer status) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setId(id);
        apiInfo.setStatus(status);
        int i = apiInfoMapper.updateById(apiInfo);
        if (i > 0) {
            redissonClient.getBucket(RedisKey.API_INFO_BY_ID + id).delete();
            return true;
        }
        return false;
    }
}
