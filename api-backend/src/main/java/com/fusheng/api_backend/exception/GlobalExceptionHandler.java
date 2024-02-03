package com.fusheng.api_backend.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.fusheng.api_backend.common.BaseResponse;
import com.fusheng.api_backend.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        return BaseResponse.error(e.getCode(), e.getMessage());
    }

    /**
     * 运行时异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return BaseResponse.error(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public BaseResponse<?> exceptionHandler(Exception e) {
        log.error("Exception", e);
        return BaseResponse.error(ErrorCode.SYSTEM_ERROR);
    }

    /**
     * 参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        return BaseResponse.error(ErrorCode.PARAMS_ERROR,fieldErrors.get(0).getDefaultMessage());
    }

    /**
     * 未登录异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    public BaseResponse<?> motLoginExceptionExceptionHandler(NotLoginException e) {
        return BaseResponse.error(ErrorCode.NOT_LOGIN_ERROR);
    }

}
