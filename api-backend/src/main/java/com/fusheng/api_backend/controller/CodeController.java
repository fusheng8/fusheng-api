package com.fusheng.api_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.fusheng.api_backend.common.BaseResponse;
import com.fusheng.api_backend.service.CodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
@Tag(name = "角色管理")
public class CodeController {
    @Resource
    private CodeService codeService;

    @Operation(summary = "获取注册验证码")
    @GetMapping("/sendRegisterCode")
    public BaseResponse sendRegisterCode(@RequestParam String email) {
        codeService.sendRegisterCode(email);
        return BaseResponse.success();
    }
}
