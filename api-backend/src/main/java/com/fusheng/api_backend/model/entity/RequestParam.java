package com.fusheng.api_backend.model.entity;

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
     * 示例值
     */
    private String exampleValue;

    /**
     * 备注
     */
    private String remark;
}
