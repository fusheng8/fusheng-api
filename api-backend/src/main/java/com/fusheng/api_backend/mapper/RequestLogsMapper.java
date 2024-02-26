package com.fusheng.api_backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fusheng.common.model.entity.RequestLogs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RequestLogsMapper extends BaseMapper<RequestLogs> {
}
