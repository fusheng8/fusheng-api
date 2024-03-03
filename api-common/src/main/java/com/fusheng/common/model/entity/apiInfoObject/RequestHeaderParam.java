package com.fusheng.common.model.entity.apiInfoObject;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestHeaderParam implements Serializable {
    /**
     * 请求头名称
     */
    private String name;

    /**
     * 请求头示例值
     */
    private String exampleValue;

    /**
     * 备注
     */
    private String remark;
}
