package com.fusheng.common.model.dto.SysUser;

import com.fusheng.common.model.common.PageQueryDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 分页查询用户
 */
@Data
public class SysUserPageQueryDTO extends PageQueryDTO implements Serializable {
    /**
     * 用户名
     */
    private String username;
    /**
     * 状态
     */
    private String userStatus;
    /**
     * 手机号
     */
    private String phone;

}
