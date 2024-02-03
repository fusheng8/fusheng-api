package com.fusheng.api_backend.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;

/**
 * 请求方法枚举
 */
public enum RequestMethodEnum {
    GET(0),
    POST(1);
    @EnumValue
    private final int type;
    RequestMethodEnum(int type) {
        this.type= type;
    }
    public int getType() {
        return type;
    }
}
