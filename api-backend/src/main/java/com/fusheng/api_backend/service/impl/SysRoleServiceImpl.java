package com.fusheng.api_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.api_backend.mapper.SysRoleMapper;
import com.fusheng.api_backend.service.SysRoleService;
import com.fusheng.common.model.dto.SysRole.SysRolePageQueryDTO;
import com.fusheng.common.model.entity.SysRole;
import com.google.gson.JsonParser;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public Page<SysRole> pageQuery(SysRolePageQueryDTO dto) {
        Page<SysRole> queryPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            queryWrapper.like("name", dto.getName());
        }

        if (dto.getOrder() != null && StringUtils.isNotBlank(dto.getColumn())) {
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

    @Override
    public List<SysRole> getAllList() {
        return sysRoleMapper.selectList(null);
    }

    @Override
    public void saveOrUpdate(SysRole sysRole) {
        if (sysRole.getId() == null) {
            sysRoleMapper.insert(sysRole);
        } else {
            sysRoleMapper.updateById(sysRole);
        }
    }

    @Override
    public boolean removeByIds(List<Long> ids) {
        return sysRoleMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public SysRole getById(Long id) {
        return sysRoleMapper.selectById(id);
    }

    @Override
    public List<String> getRoleKeysByIds(String roles) {
        List<String> rolesKey = new ArrayList<>();
        JsonParser.parseString(roles).getAsJsonArray().forEach(jsonElement -> {
            rolesKey.add(this.getById(jsonElement.getAsLong()).getRoleKey());
        });
        return rolesKey;
    }
}
