package com.fusheng.api_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fusheng.common.model.dto.SysUser.SetUserRoleDTO;
import com.fusheng.common.model.dto.SysUser.SysUserLoginDTO;
import com.fusheng.common.model.dto.SysUser.SysUserPageQueryDTO;
import com.fusheng.common.model.dto.SysUser.SysUserSaveDTO;
import com.fusheng.common.model.entity.SysUser;
import com.fusheng.common.model.vo.SysUser.SysUserInfoVO;
import com.fusheng.common.model.vo.SysUser.SysUserLoginVO;

import java.math.BigInteger;
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
     *
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

    /**
     * 保存或更新用户
     *
     * @param dto
     * @return
     */
    SysUser saveOrUpdateUser(SysUserSaveDTO dto);

    /**
     * 扣除用户余额
     *
     * @param userId
     * @param isAdd
     * @param amount
     * @return
     */
    boolean deductUserBalance(long userId, boolean isAdd, String amount);
}