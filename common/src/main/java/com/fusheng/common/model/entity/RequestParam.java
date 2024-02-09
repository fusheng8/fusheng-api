package com.fusheng.common.model.entity;

import lombok.Data;

@Data
public class RequestParam {
    /**
     * 请求参数名称
     */
    private String name;

    /**
     * 是否必填
     */
    private String required;

    /**
     * 类型
     */
    private String type;

    /**
     * 请求类型 param|body
     */
    private String requestType;

    /**
     * 备注
     */
    private String remark;
}
