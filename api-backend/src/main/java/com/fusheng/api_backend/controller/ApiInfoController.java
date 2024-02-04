package com.fusheng.api_backend.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.api_backend.common.BaseResponse;
import com.fusheng.api_backend.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.api_backend.model.dto.ApiInfo.ApiInfoSavaOrUpdateDTO;
import com.fusheng.api_backend.model.entity.ApiInfo;
import com.fusheng.api_backend.model.vo.ApiInfo.ApiInfoPageQueryVO;
import com.fusheng.api_backend.service.ApiInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "接口信息")
@RequestMapping("/api-info")
@RestController
public class ApiInfoController {
    @Resource
    private ApiInfoService apiInfoService;

    @Operation(summary = "获取所有接口信息")
    @GetMapping("/getAllApiInfo")
    public BaseResponse<List<ApiInfo>> getAllApiInfo() {
        return BaseResponse.success(apiInfoService.list());
    }

    @SaCheckRole("admin")
    @Operation(summary = "查询用户列表")
    @PostMapping("/list")
    public BaseResponse<ApiInfoPageQueryVO> list(@RequestBody ApiInfoPageQueryDTO apiInfoPageQueryDTO) {
        Page<ApiInfo> apiInfoPage = apiInfoService.pageQuery(apiInfoPageQueryDTO);
        ApiInfoPageQueryVO vo = new ApiInfoPageQueryVO();
        vo.setList(apiInfoPage.getRecords());
        vo.setTotal(apiInfoPage.getTotal());
        vo.setCurrentPage(apiInfoPage.getCurrent());
        vo.setPageSize(apiInfoPage.getSize());
        return BaseResponse.success(vo);
    }

    @Operation(summary = "批量删除接口信息")
    @GetMapping("/deleteByIds")
    public BaseResponse<Boolean> deleteByIds(@RequestParam List<Long> ids) {
        boolean res = apiInfoService.removeBatchByIds(ids);
        return BaseResponse.success(res);
    }

    @Operation(summary = "保存或者更新接口信息")
    @PostMapping("/saveOrUpdate")
    public BaseResponse<Boolean> saveOrUpdate(@RequestBody ApiInfoSavaOrUpdateDTO dto) {
        boolean res = apiInfoService.saveOrUpdateApiInfo(dto);
        return BaseResponse.success(res);
    }
}
