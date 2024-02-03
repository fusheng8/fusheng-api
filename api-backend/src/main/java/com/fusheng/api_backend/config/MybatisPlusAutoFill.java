package com.fusheng.api_backend.config;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * mybatis-plus自动填充更新时间和创建时间
 */
@Slf4j
@Component
public class MybatisPlusAutoFill implements MetaObjectHandler {

    //插入时的填充策略
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        long userId = StpUtil.getLoginIdAsLong();
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        this.strictInsertFill(metaObject, "createBy", Long.class, userId);
        this.strictInsertFill(metaObject, "updateBy", Long.class, userId);

    }

    //更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        long userId = StpUtil.getLoginIdAsLong();
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
        this.strictUpdateFill(metaObject, "updateBy", Long.class, userId);
    }
}

