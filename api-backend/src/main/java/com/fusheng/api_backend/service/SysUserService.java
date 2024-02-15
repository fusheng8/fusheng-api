package com.fusheng.api_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.common.model.dto.SysUser.SetUserRoleDTO;
import com.fusheng.common.model.dto.SysUser.SysUserLoginDTO;
import com.fusheng.common.model.dto.SysUser.SysUserPageQueryDTO;
import com.fusheng.common.model.dto.SysUser.SysUserSaveDTO;
import com.fusheng.common.model.entity.SysUser;
import com.fusheng.common.model.vo.SysUser.SysUserLoginVO;

import java.util.List;

public interface SysUserService {
    /**
     * 登录用户
     *
     * @param dto
     */
    SysUserLoginVO login(SysUserLoginDTO dto);

    /**
     * 分页查询用户
     *
     * @param dto
     * @return
     */
    Page<SysUser> pageQuery(SysUserPageQueryDTO dto);

    /**
     * 根据id获取用户
     *
     * @param id
     * @return
     */
    SysUser getById(long id);

    /**
     * 设置用户角色
     *
     * @param dto
     */
    void setUserRole(SetUserRoleDTO dto);

    /**
     * 保存或更新用户
     *
     * @param dto
     * @return
     */
    SysUser saveOrUpdate(SysUserSaveDTO dto);

    /**
     * 扣除用户余额
     *
     * @param userId
     * @param isAdd
     * @param amount
     * @return
     */
    boolean deductUserBalance(long userId, boolean isAdd, String amount);

    /**
     * 批量删除用户
     *
     * @param ids
     * @return
     */
    boolean removeByIds(List<Long> ids);

    /**
     * 根据id更新用户
     *
     * @param user
     */
    void updateById(SysUser user);
}