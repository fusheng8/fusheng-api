package com.fusheng.common.model.vo.SysUser;

import com.fusheng.common.model.common.PageQueryVO;
import com.fusheng.common.model.entity.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询用户
 */
@Data
public class SysUserPageQueryVO extends PageQueryVO implements Serializable {
    /**
     * 查询列表
     */
    private List<SysUser> list;

}
