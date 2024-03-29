package com.fusheng.common.model.dto.SysUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SysUserSaveDTO implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;


    /**
     * 邮箱
     */
    private String email;

    /**
     * 角色
     */
    private List<Long> roles;

    /**
     * 状态
     */
    private Byte userStatus;


    /**
     * 积分通知额度
     */
    private String balanceLimitNotice;

}
