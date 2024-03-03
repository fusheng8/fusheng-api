package com.fusheng.common.model.entity.apiInfoObject;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseParam implements Serializable {
    /**
     * 响应参数名称
     */
    private String name;

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
