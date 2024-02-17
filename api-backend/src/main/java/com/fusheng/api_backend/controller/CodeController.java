package com.fusheng.api_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.fusheng.api_backend.common.BaseResponse;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.service.MailService;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.common.model.entity.SysUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
@Tag(name = "角色管理")
public class CodeController {
    @Resource
    private MailService mailService;
    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "获取注册验证码")
    @GetMapping("/sendRegisterCode")
    public BaseResponse sendRegisterCode(@RequestParam String email) {
        mailService.sendRegisterCode(email);
        return BaseResponse.success();
    }

    @Operation(summary = "获取重置sk验证码")
    @GetMapping("/sendResetSkCode")
    @SaCheckLogin
    public BaseResponse sendResetSkCode() {
        long id = StpUtil.getLoginIdAsLong();
        SysUser user = sysUserService.getById(id);
        if (user == null) {
            return BaseResponse.error(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        mailService.sendResetSkCode(user.getEmail());
        return BaseResponse.success();
    }
    @Operation(summary = "获取邮箱登录验证码")
    @GetMapping("/sendEmailLoginCode")
    public BaseResponse sendEmailLoginCode(@RequestParam @Validated
                                               @Email(message = "邮箱格式错误") String email) {
        mailService.sendEmailLoginCode(email);
        return BaseResponse.success();
    }
}
