package com.fusheng.common.model.dto.SysUser;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 */
@Data
@Builder
public class AddSysUserDTO implements Serializable {

    /**
     * 用户账号
     */
    @NotBlank(message = "用户账号不能为空")
    private String useraccount;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度为6-20位")
    private String password;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 角色
     */
    private String roles;

    /**
     * 状态
     */
    private String userStatus;

}