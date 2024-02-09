package com.fusheng.common.model.entity;

import com.fusheng.common.model.enums.RequestMethodEnum;
import lombok.Data;

/**
 * api信息的对象版本（所有json字段转换成对象）
 */
@Data
public class ApiInfoObject {
    /**
     * id
     */
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
    private RequestParam requestParams;

    /**
     * 接口响应参数
     */
    private ResponseParam responseParams;

    /**
     * 扣除积分数
     */
    private Long reduceBalance;

    /**
     * 请求示例
     */
    private String requestExample;

    /**
     * 请求头
     */
    private RequestHeaderParam requestHeader;

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
}
