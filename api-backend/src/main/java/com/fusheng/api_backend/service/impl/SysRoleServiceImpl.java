package com.fusheng.api_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fusheng.api_backend.mapper.SysRoleMapper;
import com.fusheng.common.model.dto.SysRole.SysRolePageQueryDTO;
import com.fusheng.common.model.entity.SysRole;
import com.fusheng.api_backend.service.SysRoleService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;


@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
   @Resource
    private SysRoleMapper sysRoleMapper;
    @Override
    public Page<SysRole> pageQuery(SysRolePageQueryDTO dto) {
        Page<SysRole> queryPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            queryWrapper.like("name", dto.getName());
        }

        if (dto.getOrder()!=null&&StringUtils.isNotBlank(dto.getColumn())) {
            switch (dto.getOrder()) {
                case asc:
                    queryWrapper.orderByAsc(dto.getColumn());
                    break;
                case desc:
                    queryWrapper.orderByDesc(dto.getColumn());
                    break;
            }
        }

        Page<SysRole> page = sysRoleMapper.selectPage(queryPage, queryWrapper);

        return page;
    }
}
