package com.fusheng.api_backend.model.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 用户表
 */
@Data
@TableName(value = "sys_user")
public class SysUser {
    /**
    * 主键
    */
    @TableId(type = IdType.AUTO)
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
    private String roles;

    /**
    * 状态
    */
    private Integer userStatus;
    /**
     * 积分
     */
    private Integer balance;

    /**
    * 是否删除(0-未删, 1-已删)
    */
    @TableLogic
    private Integer isDeleted;

    /**
    * 更新时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
    * 创建者
    */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
    * 更新者
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
}