package com.fusheng.common.model.dto.SysUser;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserLoginDTO implements Serializable {
    /**
     * 登录方式 account-账号登录 email-邮箱登录
     */
    @NotBlank(message = "登录方式不能为空")
    private String type;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码
     */
    private String email;
    /**
     * 密码
     */
    private String code;

}
