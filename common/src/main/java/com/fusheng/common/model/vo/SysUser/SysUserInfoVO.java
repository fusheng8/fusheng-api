package com.fusheng.common.model.vo.SysUser;


import lombok.Data;

import java.util.List;

@Data
public class SysUserInfoVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 角色
     */
    private List<String> roles;

    /**
     * 状态
     */
    private Integer userStatus;

    /**
     * 积分
     */
    private String balance;

    /**
     * AccessKey
     */
    private String accessKey;

    /**
     * SecretKey
     */
    private String secretKey;

}
