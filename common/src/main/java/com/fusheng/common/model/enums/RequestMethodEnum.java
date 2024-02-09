package com.fusheng.common.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;
import lombok.Getter;

/**
 * 请求方法枚举
 */
@Getter
public enum RequestMethodEnum {
    GET(0),
    POST(1);
    @EnumValue
    private final int type;
    RequestMethodEnum(int type) {
        this.type= type;
    }
}
