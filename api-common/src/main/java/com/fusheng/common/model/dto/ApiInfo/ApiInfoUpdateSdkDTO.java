package com.fusheng.common.model.dto.ApiInfo;

import com.fusheng.common.model.entity.apiSdkObject.ApiSdkItem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ApiInfoUpdateSdkDTO implements Serializable {
    /**
     * id
     */
    @NotNull(message = "id不能为空")
    long id;
    /**
     * 新的sdk值
     */
    @NotNull(message = "sdk不能为空")
    List<ApiSdkItem> sdk;
}
