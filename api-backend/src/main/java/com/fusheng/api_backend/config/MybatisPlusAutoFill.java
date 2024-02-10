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

        LocalDateTime hasCreateTime = (LocalDateTime) this.getFieldValByName("createTime", metaObject);
        LocalDateTime hasUpdateTime = (LocalDateTime) this.getFieldValByName("updateTime", metaObject);
        Long hasCreateBy = (Long) this.getFieldValByName("createBy", metaObject);
        Long hasUpdateBy = (Long) this.getFieldValByName("updateBy", metaObject);
        if (hasCreateTime == null) {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, now);
        }
        if (hasUpdateTime == null) {
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, now);
        }
        if (hasCreateBy == null) {
            long userId = StpUtil.getLoginIdAsLong();
            this.strictInsertFill(metaObject, "createBy", Long.class, userId);
        }
        if (hasUpdateBy == null) {
            long userId = StpUtil.getLoginIdAsLong();
            this.strictInsertFill(metaObject, "updateBy", Long.class, userId);
        }

    }

    //更新时的填充策略
    @Override
    public void updateFill(MetaObject metaObject) {

        LocalDateTime hasUpdateTime = (LocalDateTime) this.getFieldValByName("updateTime", metaObject);
        Long hasUpdateBy = (Long) this.getFieldValByName("updateBy", metaObject);

        if (hasUpdateTime == null) {
            LocalDateTime now = LocalDateTime.now();
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, now);
        }

        if (hasUpdateBy == null) {
            long userId = StpUtil.getLoginIdAsLong();
            this.strictUpdateFill(metaObject, "updateBy", Long.class, userId);
        }
    }
}

