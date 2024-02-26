package com.fusheng.common.model.vo.SysRole;

import com.fusheng.common.model.common.PageQueryVO;
import com.fusheng.common.model.entity.SysRole;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询用户
 */
@Data
public class SysRolePageQueryVO extends PageQueryVO implements Serializable {
    /**
     * 查询列表
     */
    private List<SysRole> list;


}
