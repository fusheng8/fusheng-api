package com.fusheng.api_backend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.mapper.SysRoleMapper;
import com.fusheng.api_backend.mapper.SysUserMapper;
import com.fusheng.api_backend.model.dto.sysUser.SetUserRoleDTO;
import com.fusheng.api_backend.model.dto.sysUser.SysUserLoginDTO;
import com.fusheng.api_backend.model.dto.sysUser.SysUserPageQueryDTO;
import com.fusheng.api_backend.model.entity.SysUser;
import com.fusheng.api_backend.model.vo.sysUser.SysUserInfoVO;
import com.fusheng.api_backend.model.vo.sysUser.SysUserLoginVO;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.api_backend.utils.PasswordUtil;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public SysUserLoginVO login(SysUserLoginDTO sysUserLoginDTO) {
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("username", sysUserLoginDTO.getUsername());
        SysUser sysUser = sysUserMapper.selectOne(sysUserQueryWrapper);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        //加密密码

        String encryptPassword = PasswordUtil.encrypt(sysUserLoginDTO.getPassword());
        if (!sysUser.getPassword().equals(encryptPassword)) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        StpUtil.login(sysUser.getId());
        SysUserLoginVO sysUserLoginVO = new SysUserLoginVO();
        sysUserLoginVO.setToken(StpUtil.getTokenInfo().getTokenValue());
        return sysUserLoginVO;
    }

    public List<String> getRoleKeysByIds(String roleStr) {
        //将角色id列表转换为字符串
        List<Long> roleIds = new ArrayList<>();
        JsonParser.parseString(roleStr).getAsJsonArray().forEach(jsonElement -> {
            roleIds.add(jsonElement.getAsLong());
        });

        //根据角色id列表查询角色名称列表
        List<String> roles = new ArrayList<>();
        sysRoleMapper.selectBatchIds(roleIds).forEach(sysRole -> {
            roles.add(sysRole.getRoleKey());
        });
        return roles;
    }

    @Override
    public Page<SysUser> pageQuery(SysUserPageQueryDTO sysUserPageQueryDTO) {
        Page<SysUser> queryPage = new Page<>(sysUserPageQueryDTO.getCurrent(), sysUserPageQueryDTO.getPageSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(sysUserPageQueryDTO.getUsername())) {
            queryWrapper.like("username", sysUserPageQueryDTO.getUsername());
        }
        if (StringUtils.isNotBlank(sysUserPageQueryDTO.getPhone())) {
            queryWrapper.like("phone", sysUserPageQueryDTO.getPhone());
        }
        if (StringUtils.isNotBlank(sysUserPageQueryDTO.getUserStatus())) {
            queryWrapper.like("user_status",
                    sysUserPageQueryDTO.getUserStatus().equals("1") ? 1 : 0);
        }
        Page<SysUser> page = sysUserMapper.selectPage(queryPage, queryWrapper);
        //脱敏
        for (SysUser record : page.getRecords()) {
            record.setPassword(null);
        }
        return page;
    }

    @Override
    public void setUserRole(SetUserRoleDTO setUserRoleDTO) {
        SysUser sysUser = new SysUser();
        sysUser.setId(setUserRoleDTO.getUserId());
        sysUser.setRoles(new Gson().toJson(setUserRoleDTO.getRoleIds()));
        sysUserMapper.updateById(sysUser);
    }

    @Override
    public SysUserInfoVO getUserInfoById(long id) {
        SysUser user = sysUserMapper.selectById(id);
        SysUserInfoVO sysUserInfoVO = new SysUserInfoVO();
        BeanUtils.copyProperties(user, sysUserInfoVO);

        //转换角色id为角色key
        List<String> roleKeys = getRoleKeysByIds(user.getRoles());
        sysUserInfoVO.setRoles(roleKeys);
        //脱敏
        sysUserInfoVO.setPassword(null);
        return sysUserInfoVO;
    }
}
