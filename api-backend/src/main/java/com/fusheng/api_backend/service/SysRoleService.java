package com.fusheng.api_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.common.model.dto.SysRole.SysRolePageQueryDTO;
import com.fusheng.common.model.entity.SysRole;

import java.util.List;

public interface SysRoleService {
    /**
     * 分页查询角色
     *
     * @param dto
     * @return
     */
    Page<SysRole> pageQuery(SysRolePageQueryDTO dto);

    /**
     * 获取所有角色
     *
     * @return
     */
    List<SysRole> getAllList();

    /**
     * 保存或者更新角色
     *
     * @param sysRole
     */
    void saveOrUpdate(SysRole sysRole);

    /**
     * 根据id批量删除角色
     *
     * @param ids
     */
    boolean removeByIds(List<Long> ids);

    /**
     * 根据id获取角色
     *
     * @param id
     * @return
     */
    SysRole getById(Long id);

    /**
     * 根据id获取角色key
     *
     * @param roles json数组
     * @return
     */
    List<String> getRoleKeysByIds(String roles);
}
