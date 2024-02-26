package com.fusheng.common.model.dto.withdraw;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;

@Data
public class WithdrawDTO implements Serializable {

    /**
     * 提现积分
     */
    @NotBlank(message = "提现积分不能为空")
    @Positive(message = "提现积分必须为正am整数")
    private String amount;

    /**
     * 提现方式 1-支付宝 2-微信
     */
    private Integer method;

    /**
     * 收款码链接
     */
    private String paymentCode;
}
