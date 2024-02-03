package com.fusheng.api_backend.model.common;

import lombok.Data;

/**
 * 分页查询DTO
 */
@Data
public class PageQueryDTO {
    /**
     * 第几页
     */
    private Integer current;
    /**
     * 每页多少条
     */
    private Integer pageSize;

}
