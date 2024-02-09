package com.fusheng.common.model.vo.ApiInfo;

import com.fusheng.common.model.common.PageQueryVO;
import com.fusheng.common.model.entity.ApiInfo;
import lombok.Data;

import java.util.List;

/**
 * 分页查询用户
 */
@Data
public class ApiInfoPageQueryVO extends PageQueryVO {
    /**
     * 查询列表
     */
    private List<ApiInfo> list;


}
