package com.fusheng.common.model.dto.SysUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SysUserLoginDTO {
    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20位")
    private String password;

}
