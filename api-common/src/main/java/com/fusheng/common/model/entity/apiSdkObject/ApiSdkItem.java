package com.fusheng.common.model.entity.apiSdkObject;

import lombok.Data;

import java.util.List;

@Data
public class ApiSdkItem {
    /**
     * 语言
     */
    String language;

    /**
     * sdk
     */
    List<ApiSdkObject> list;
}
