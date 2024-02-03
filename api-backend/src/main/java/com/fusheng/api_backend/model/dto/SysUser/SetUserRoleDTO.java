package com.fusheng.api_backend.model.dto.SysUser;

import lombok.Data;

import java.util.List;

@Data
public class SetUserRoleDTO {
    /**
     * 用户id
     */
    private long userId;

    /**
     * 角色id
     */
    private List<Long> roleIds;
}
