package com.fusheng.common.model.dto.SysRole;

import com.fusheng.common.model.common.PageQueryDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询角色
 */
@Data
public class SysRolePageQueryDTO extends PageQueryDTO implements Serializable {
    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色权限字符串
     */
    private String roleKey;

    /**
     * 角色状态（1正常 0停用）
     */
    private String status;

}
