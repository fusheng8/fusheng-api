package com.fusheng.api_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.fusheng.api_backend.common.BaseResponse;
import com.fusheng.api_backend.model.entity.ApiInfo;
import com.fusheng.api_backend.service.ApiInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
