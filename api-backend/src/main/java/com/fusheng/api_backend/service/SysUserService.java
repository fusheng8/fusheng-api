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
     * 根据用户id列表查询角色名称列表
     * @param dto
     * @return
     */
    List<String> getRoleKeysByIds(String dto);

    /**
     * 设置用户角色
     *
     * @param dto
     */
    void setUserRole(SetUserRoleDTO dto);

    /***
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    SysUserInfoVO getUserInfoById(long id);
}