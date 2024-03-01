package com.fusheng.api_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.api_backend.common.BaseResponse;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.service.ApiInfoService;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoSavaOrUpdateDTO;
import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.vo.ApiInfo.ApiInfoPageQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "接口信息")
@RequestMapping("/api-info")
@RestController
public class ApiInfoController {
    @Resource
    private ApiInfoService apiInfoService;

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

    @SaCheckLogin
    @Operation(summary = "批量删除接口信息")
    @GetMapping("/deleteByIds")
    public BaseResponse<Boolean> deleteByIds(@RequestParam List<Long> ids) {
        if (!StpUtil.hasRole("admin")) {
            //如果不是是管理员，只能删除自己的接口
            List<ApiInfo> apiInfoList = apiInfoService.queryByIds(ids);
            long userId = StpUtil.getLoginIdAsLong();
            for (ApiInfo apiInfo : apiInfoList) {
                if (userId != apiInfo.getUserId()) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "没有权限删除别人的接口");
                }
            }
        }

        boolean res = apiInfoService.removeByIds(ids);
        return BaseResponse.success(res);
    }

    @SaCheckLogin
    @Operation(summary = "保存或者更新接口信息")
    @PostMapping("/saveOrUpdate")
    public BaseResponse<Boolean> saveOrUpdate(@RequestBody ApiInfoSavaOrUpdateDTO dto) {
        //如果不是管理员强制修改状态为待审核
        if (!StpUtil.hasRole("admin")) {
            dto.setStatus(0);
        }
        boolean res = apiInfoService.saveOrUpdate(dto);
        return BaseResponse.success(res);
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/queryById")
    public BaseResponse<ApiInfo> queryById(Long id) {
        if (id == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        return BaseResponse.success(apiInfoService.getById(id));
    }

    @Operation(summary = "管理员审核接口")
    @GetMapping("/reviewApi")
    @SaCheckRole("admin")
    public BaseResponse<Boolean> reviewApi(@RequestParam Long id, @RequestParam Integer status) {
        if (id == null || status == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        return BaseResponse.success(apiInfoService.reviewApi(id, status));
    }

    @Operation(summary = "生成sdk")
    @GetMapping("/generateSdk")
    public BaseResponse<String> generateSdk(@RequestParam @Validated @NotNull(message = "id不能为空") Long id) {

        return BaseResponse.success(apiInfoService.generateSdk(id));
    }
}
