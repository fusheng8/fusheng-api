package com.fusheng.api_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fusheng.api_backend.model.dto.SysUser.SetUserRoleDTO;
import com.fusheng.api_backend.model.dto.SysUser.SysUserLoginDTO;
import com.fusheng.api_backend.model.dto.SysUser.SysUserPageQueryDTO;
import com.fusheng.api_backend.model.entity.SysUser;
import com.fusheng.api_backend.model.vo.SysUser.SysUserInfoVO;
import com.fusheng.api_backend.model.vo.SysUser.SysUserLoginVO;

import java.util.List;

public interface SysUserService extends IService<SysUser> {
    /**
     * 登录用户
     *
     * @param sysUserLoginDTO
     */
    SysUserLoginVO login(SysUserLoginDTO sysUserLoginDTO);

    /**
     * 分页查询用户
     *
     * @param sysUserPageQueryDTO
     * @return
     */
    Page<SysUser> pageQuery(SysUserPageQueryDTO sysUserPageQueryDTO);


    /**
     * 根据用户id列表查询角色名称列表
     * @param roleStr
     * @return
     */
    List<String> getRoleKeysByIds(String roleStr);

    /**
     * 设置用户角色
     *
     * @param setUserRoleDTO
     */
    void setUserRole(SetUserRoleDTO setUserRoleDTO);

    /***
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    SysUserInfoVO getUserInfoById(long id);
}