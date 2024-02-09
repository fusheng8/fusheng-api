package com.fusheng.common.model.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.fusheng.common.model.enums.RequestMethodEnum;
import lombok.Data;

/**
 * 接口信息
 */
@Data
@TableName(value = "api_info")
public class ApiInfo {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口头像
     */
    private String avatarUrl;

    /**
     * 发布人
     */
    private Long userId;

    /**
     * 请求方法 0-get 1-post
     */
    private RequestMethodEnum method;

    /**
     * 接口请求参数
     */
    private String requestParams;

    /**
     * 接口响应参数
     */
    private String responseParams;

    /**
     * 扣除积分数
     */
    private String reduceBalance;

    /**
     * 请求示例
     */
    private String requestExample;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应示例
     */
    private String responseExample;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 接口状态（0- 默认下线 1- 上线）
     */
    private Integer status;

    /**
     * sdk
     */
    private String sdk;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Byte isDeleted;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}