package com.fusheng.api_backend.model.vo.SysRole;

import com.fusheng.api_backend.model.common.PageQueryVO;
import com.fusheng.api_backend.model.entity.SysRole;
import lombok.Data;

import java.util.List;

/**
 * 分页查询用户
 */
@Data
public class SysRolePageQueryVO extends PageQueryVO {
    /**
     * 查询列表
     */
    private List<SysRole> list;


}
