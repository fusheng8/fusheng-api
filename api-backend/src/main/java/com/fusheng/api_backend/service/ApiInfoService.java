package com.fusheng.api_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoSavaOrUpdateDTO;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoUpdateSdkDTO;
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
     * 批量删除接口信息
     *
     * @param ids
     * @return
     */
    boolean removeByIds(List<Long> ids);

    /**
     * 根据url获取接口信息
     *
     * @param mappingUrl
     * @return
     */
    ApiInfo getByMappingUrl(String mappingUrl);

    /**
     * 根据id批量查询接口信息
     *
     * @param ids
     * @return
     */
    List<ApiInfo> queryByIds(List<Long> ids);

    /**
     * 审核接口
     *
     * @param id
     * @param status
     * @return
     */
    Boolean reviewApi(Long id, Integer status);

    /**
     * 根据接口id生成sdk
     *
     * @param id
     * @return
     */
    String generateSdk(Long id);

    /**
     * 修改sdk
     *
     * @param dto
     */
    void updateSdk(ApiInfoUpdateSdkDTO dto);

    /**
     * 获取所有接口信息
     *
     * @return
     */
    List<ApiInfo> getAllList();
}
