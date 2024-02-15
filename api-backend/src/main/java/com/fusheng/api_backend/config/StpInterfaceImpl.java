package com.fusheng.api_backend.config;

import cn.dev33.satoken.stp.StpInterface;
import com.fusheng.api_backend.service.SysRoleService;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SysUser user = sysUserService.getById(Long.parseLong(loginId.toString()));
        return sysRoleService.getRoleKeysByIds(user.getRoles());
    }

}
