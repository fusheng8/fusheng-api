package com.fusheng.api_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fusheng.api_backend.model.entity.ApiInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiInfoMapper extends BaseMapper<ApiInfo> {
}