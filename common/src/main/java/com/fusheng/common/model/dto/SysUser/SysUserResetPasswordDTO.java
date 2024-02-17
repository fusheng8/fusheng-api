package com.fusheng.common.model.dto.SysUser;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserResetPasswordDTO implements Serializable {
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误")
    private String email;
    /**
     * 密码
     */
    @Size(min = 6, max = 20, message = "密码长度为6-20")
    private String password;
    /**
     * 验证码
     */
    @NotBlank
    private String code;
}
