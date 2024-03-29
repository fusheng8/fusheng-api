package com.fusheng.common.model.common;

import com.fusheng.common.model.enums.OrderEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询DTO
 */
@Data
public class PageQueryDTO implements Serializable {
    /**
     * 第几页
     */
    private Integer current;
    /**
     * 每页多少条
     */
    private Integer pageSize;
    /**
     * 排序字段
     */
    private String column;
    /**
     * 排序方式
     */
    private OrderEnum order;

}
