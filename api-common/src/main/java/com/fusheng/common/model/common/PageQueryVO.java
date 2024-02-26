package com.fusheng.common.model.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询DTO
 */
@Data
public class PageQueryVO implements Serializable {
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
