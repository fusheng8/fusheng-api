package com.fusheng.common.model.dto.SysUser;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SetUserRoleDTO implements Serializable {
    /**
     * 用户id
     */
    private long userId;

    /**
     * 角色id
     */
    private List<Long> roleIds;
}
