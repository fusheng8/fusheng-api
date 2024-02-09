package com.fusheng.common.model.dto.ApiInfo;

import com.fusheng.common.model.common.PageQueryDTO;
import com.fusheng.common.model.enums.RequestMethodEnum;
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
     * 接口状态
     */
    private Integer status;

    /**
     * 请求方式
     */
    private RequestMethodEnum method;

}
