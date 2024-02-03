package com.fusheng.api_backend.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色信息表
 */
@Data
@TableName(value = "sys_role")
public class SysRole implements Serializable {
    @TableId(type = IdType.AUTO)
    /**
    * 角色ID
    */
    private Long id;

    /**
    * 角色名称
    */
    private String name;

    /**
    * 角色权限字符串
    */
    private String roleKey;

    /**
    * 角色状态（1正常 0停用）
    */
    private Byte status;

    /**
    * 备注
    */
    private String remark;

    /**
    * 删除标志（0代表存在 2代表删除）
    */
    @TableLogic
    private Byte isDeleted;

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

    /**
    * 更新时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}