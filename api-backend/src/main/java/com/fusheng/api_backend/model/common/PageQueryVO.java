package com.fusheng.api_backend.model.common;

import lombok.Data;

/**
 * 分页查询DTO
 */
@Data
public class PageQueryVO {
    /**
     * 总数
     */
    private long total;
    /**
     * 第几页
     */
    private long currentPage;
    /**
     * 每页多少条
     */
    private long pageSize;

}
