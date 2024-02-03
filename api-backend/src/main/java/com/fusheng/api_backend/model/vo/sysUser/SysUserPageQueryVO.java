package com.fusheng.api_backend.model.vo.sysUser;

import com.fusheng.api_backend.model.common.PageQueryVO;
import com.fusheng.api_backend.model.entity.SysUser;
import lombok.Data;

import java.util.List;

/**
 * 分页查询用户
 */
@Data
public class SysUserPageQueryVO extends PageQueryVO {
    /**
     * 查询列表
     */
    private List<SysUser> list;

}
