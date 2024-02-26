package com.fusheng.common.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 请求日志
 */
@Data
@TableName("request_logs")
public class RequestLogs implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer logId;

    /**
     * URL
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求头
     */
    private String requestHeaders;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 响应码
     */
    private Integer responseCode;

    /**
     * 响应头
     */
    private String responseHeaders;

    /**
     * 响应体
     */
    private String responseBody;

    /**
     * 日志时间戳，默认为当前时间
     */
    private Date timestamp;

}