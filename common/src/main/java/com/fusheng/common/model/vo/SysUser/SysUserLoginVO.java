package com.fusheng.common.model.vo.SysUser;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysUserLoginVO implements Serializable {
    private String token;
}
