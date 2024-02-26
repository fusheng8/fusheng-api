package com.fusheng.common.model.dto.ApiInfo;

import com.fusheng.common.model.entity.RequestHeaderParam;
import com.fusheng.common.model.entity.RequestParam;
import com.fusheng.common.model.entity.ResponseParam;
import com.fusheng.common.model.enums.RequestMethodEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ApiInfoSavaOrUpdateDTO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 接口头像
     */
    private String avatarUrl;

    /**
     * 请求方法 0-get 1-post
     */
    private RequestMethodEnum method;

    /**
     * 接口请求参数
     */
    private List<RequestParam> requestParams;

    /**
     * 接口响应参数
     */
    private List<ResponseParam> responseParams;

    /**
     * 扣除积分数
     */
    private String reduceBalance;

    /**
     * 请求示例
     */
    private String requestExample;

    /**
     * 请求头
     */
    private List<RequestHeaderParam> requestHeader;

    /**
     * 响应示例
     */
    private String responseExample;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 接口状态（0- 默认下线 1- 上线）
     */
    private Integer status;

    /**
     * sdk
     */
    private String sdk;
}
