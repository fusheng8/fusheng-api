package com.fusheng.api_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusheng.api_backend.mapper.SysRoleMapper;
import com.fusheng.api_backend.model.dto.sysRole.SysRolePageQueryDTO;
import com.fusheng.api_backend.model.entity.SysRole;
import com.fusheng.api_backend.service.SysRoleService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
   @Resource
    private SysRoleMapper sysRoleMapper;
    @Override
    public Page<SysRole> pageQuery(SysRolePageQueryDTO sysRolePageQueryDTO) {
        Page<SysRole> queryPage = new Page<>(sysRolePageQueryDTO.getCurrent(), sysRolePageQueryDTO.getPageSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(sysRolePageQueryDTO.getName())) {
            queryWrapper.like("name", sysRolePageQueryDTO.getName());
        }
        if (StringUtils.isNotBlank(sysRolePageQueryDTO.getRoleKey())) {
            queryWrapper.like("role_key", sysRolePageQueryDTO.getRoleKey());
        }
        if (StringUtils.isNotBlank(sysRolePageQueryDTO.getStatus())) {
            queryWrapper.like("status",
                    sysRolePageQueryDTO.getStatus().equals("1") ? 1 : 0);
        }
        Page<SysRole> page = sysRoleMapper.selectPage(queryPage, queryWrapper);

        return page;
    }
}
