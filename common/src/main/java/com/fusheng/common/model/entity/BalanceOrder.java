package com.fusheng.common.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@TableName(value = "balance_order")
public class BalanceOrder {

    /**
     * 订单编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单名称
     */
    private String name;

    /**
     * 订单金额
     */
    private String amount;

    /**
     * 支付状态（0：未支付，1：已支付，2：已取消）
     */
    private Integer state;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;


    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
