package com.fusheng.common.model.entity;

import lombok.Data;

@Data
public class RequestHeaderParam {
    /**
     * 请求头名称
     */
    private String name ;

    /**
     * 请求头示例值
     */
    private String exampleValue;

    /**
     * 备注
     */
    private String remark;
}
