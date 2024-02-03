package com.fusheng.api_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fusheng.api_backend.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.api_backend.model.entity.ApiInfo;
import com.fusheng.api_backend.model.vo.ApiInfo.ApiInfoPageQueryVO;

public interface ApiInfoService extends IService<ApiInfo> {
    /**
     * 分页查询
     *
     * @param apiInfoPageQueryDTO
     * @return
     */
    Page<ApiInfo> pageQuery(ApiInfoPageQueryDTO apiInfoPageQueryDTO);
}
