package com.fusheng.api_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusheng.api_backend.mapper.SysRoleMapper;
import com.fusheng.api_backend.model.dto.SysRole.SysRolePageQueryDTO;
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

        if (sysRolePageQueryDTO.getOrder()!=null&&StringUtils.isNotBlank(sysRolePageQueryDTO.getColumn())) {
            switch (sysRolePageQueryDTO.getOrder()) {
                case asc:
                    queryWrapper.orderByAsc(sysRolePageQueryDTO.getColumn());
                    break;
                case desc:
                    queryWrapper.orderByDesc(sysRolePageQueryDTO.getColumn());
                    break;
            }
        }

        Page<SysRole> page = sysRoleMapper.selectPage(queryPage, queryWrapper);

        return page;
    }
}
