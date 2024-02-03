package com.fusheng.api_backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String msg;

    public BaseResponse(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }

    public static BaseResponse success() {
        return new BaseResponse(ErrorCode.SUCCESS);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse(ErrorCode.SUCCESS.getCode(), data, ErrorCode.SUCCESS.getMessage());
    }

    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse(errorCode);
    }
    public static BaseResponse error(ErrorCode errorCode,String message) {
        return BaseResponse.error(errorCode.getCode(),message);
    }

    public static BaseResponse error(int code, String message) {
        return new BaseResponse(code, null, message);
    }
}
