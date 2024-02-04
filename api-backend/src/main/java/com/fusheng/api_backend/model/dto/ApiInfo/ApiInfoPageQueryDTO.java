package com.fusheng.api_backend.model.dto.ApiInfo;

import com.fusheng.api_backend.model.common.PageQueryDTO;
import com.fusheng.api_backend.model.enums.RequestMethodEnum;
import lombok.Data;

/**
 * 分页查询角色
 */
@Data
public class ApiInfoPageQueryDTO extends PageQueryDTO {
    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口费用
     */
    private Integer status;

    /**
     * 请求方式
     */
    private RequestMethodEnum method;

}