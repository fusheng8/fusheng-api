package com.fusheng;

import com.fusheng.common.model.entity.SysUser;

public interface AuthorizeService {

    /**
     * 根据accessKey获取用户信息
     *
     * @param accessKey
     * @return
     */
    SysUser getUserByAccessKey(String accessKey);

}