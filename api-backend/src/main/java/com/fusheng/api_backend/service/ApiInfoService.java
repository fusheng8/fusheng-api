package com.fusheng.api_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoSavaOrUpdateDTO;
import com.fusheng.common.model.entity.ApiInfo;

public interface ApiInfoService extends IService<ApiInfo> {
    /**
     * 分页查询
     *
     * @param dto
     * @return
     */
    Page<ApiInfo> pageQuery(ApiInfoPageQueryDTO dto);

    /**
     * 保存或者更新接口信息
     */
    boolean saveOrUpdateApiInfo(ApiInfoSavaOrUpdateDTO dto);
}
