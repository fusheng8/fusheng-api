package com.fusheng.api_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoSavaOrUpdateDTO;
import com.fusheng.common.model.entity.ApiInfo;

import java.util.List;

public interface ApiInfoService {
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
    boolean saveOrUpdate(ApiInfoSavaOrUpdateDTO dto);

    /**
     * 根据id获取接口信息
     *
     * @param id
     * @return
     */
    ApiInfo getById(Long id);

    /**
     * 获取所有接口信息
     *
     * @return
     */
    List<ApiInfo> getAllList();

    /**
     * 批量删除接口信息
     *
     * @param ids
     * @return
     */
    boolean removeByIds(List<Long> ids);

    /**
     * 根据url获取接口信息
     *
     * @param apiUrl
     * @return
     */
    ApiInfo getByUrl(String apiUrl);
}
