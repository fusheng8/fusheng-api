package com.fusheng.api_backend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.mapper.SysUserMapper;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.api_backend.utils.PasswordUtil;
import com.fusheng.common.constant.RedisName;
import com.fusheng.common.model.dto.SysUser.*;
import com.fusheng.common.model.entity.SysUser;
import com.fusheng.common.model.vo.SysUser.SysUserLoginVO;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RedissonClient redissonClient;

    @Override
    public SysUserLoginVO login(SysUserLoginDTO dto) {
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("username", dto.getUsername());
        SysUser sysUser = sysUserMapper.selectOne(sysUserQueryWrapper);
        if (sysUser == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_OR_PASSWORD_ERROR);
        }

        //加密密码
        String encryptPassword = PasswordUtil.encrypt(dto.getPassword());
        if (!sysUser.getPassword().equals(encryptPassword)) {
            throw new BusinessException(ErrorCode.ACCOUNT_OR_PASSWORD_ERROR);
        }
        StpUtil.login(sysUser.getId());
        SysUserLoginVO sysUserLoginVO = new SysUserLoginVO();
        sysUserLoginVO.setToken(StpUtil.getTokenInfo().getTokenValue());
        return sysUserLoginVO;
    }

    @Override
    public void register(SysUserRegisterDTO dto) {
        //验证验证码
        RBucket<String> bucket = redissonClient.getBucket(RedisName.CODE_REGISTER + dto.getEmail());
        if (!dto.getCode().equals(bucket.get())) {
            throw new BusinessException(ErrorCode.CODE_ERROR);
        }

        //验证用户名是否重复
        QueryWrapper<SysUser> sysUserQueryWrapper = new QueryWrapper<>();
        sysUserQueryWrapper.eq("username", dto.getUsername());
        if (sysUserMapper.selectOne(sysUserQueryWrapper) != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户名已存在");
        }

        SysUserSaveDTO sysUserSaveDTO = new SysUserSaveDTO();
        BeanUtils.copyProperties(dto, sysUserSaveDTO);
        //设置默认信息
        sysUserSaveDTO.setUserStatus((byte) 1);
        sysUserSaveDTO.setRoles(List.of(2L));
        this.saveOrUpdate(sysUserSaveDTO,true);
    }

    @Override
    public SysUser getById(long id) {
        RBucket<SysUser> bucket = redissonClient.getBucket(RedisName.USER_BY_ID + id);
        if (bucket.isExists()) {
            return bucket.get();
        }
        SysUser user = sysUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        user.setPassword(null);
        bucket.set(user);
        return user;
    }

    @Override
    public Page<SysUser> pageQuery(SysUserPageQueryDTO dto) {
        Page<SysUser> queryPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getUsername())) {
            queryWrapper.like("username", dto.getUsername());
        }
        if (StringUtils.isNotBlank(dto.getPhone())) {
            queryWrapper.like("phone", dto.getPhone());
        }
        if (StringUtils.isNotBlank(dto.getUserStatus())) {
            queryWrapper.like("user_status",
                    dto.getUserStatus().equals("1") ? 1 : 0);
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
        Page<SysUser> page = sysUserMapper.selectPage(queryPage, queryWrapper);
        //脱敏
        for (SysUser record : page.getRecords()) {
            record.setPassword(null);
        }
        return page;
    }

    @Override
    public void setUserRole(SetUserRoleDTO dto) {
        SysUser sysUser = new SysUser();
        sysUser.setId(dto.getUserId());
        sysUser.setRoles(new Gson().toJson(dto.getRoleIds()));
        sysUserMapper.updateById(sysUser);
        //修改用户缓存
        redissonClient.getBucket(RedisName.USER_BY_ID + dto.getUserId()).set(sysUser);
    }

    @Override
    public SysUser saveOrUpdate(SysUserSaveDTO dto,boolean isRegister) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        if (StringUtils.isNoneEmpty(user.getPassword())) {
            //密码加密
            String password = PasswordUtil.encrypt(user.getPassword());
            user.setPassword(password);
        }
        if (dto.getRoles() != null) {
            user.setRoles(new Gson().toJson(dto.getRoles()));
        }
        if (dto.getId() != null) {
            //更新操作
            if (isRegister){
                //如果是注册模式则手动赋值修改者、修改时间
                user.setUpdateBy(0L);
                user.setUpdateTime(LocalDateTime.now());
            }
            sysUserMapper.updateById(user);
            //修改用户缓存
            redissonClient.getBucket(RedisName.USER_BY_ID + dto.getId()).set(user);
        } else {
            //新增操作
            user.setAccessKey(RandomUtil.randomString(16));
            user.setSecretKey(RandomUtil.randomString(32));
            if (isRegister){
                //如果是注册模式则手动赋值创建者、创建时间、修改者、修改时间
                user.setCreateBy(0L);
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateBy(0L);
                user.setUpdateTime(LocalDateTime.now());
            }
            sysUserMapper.insert(user);
        }

        return user;
    }

    @Override
    public boolean deductUserBalance(long userId, boolean isAdd, String amount) {
        // 对用户加锁
        RLock lock = redissonClient.getLock(RedisName.USER_BALANCE_LOCK + userId);
        try {
            // 只有一个线程能获取到锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                SysUser user = sysUserMapper.selectById(userId);
                // 判断余额是否足够
                BigInteger bigInteger1 = new BigInteger(user.getBalance());
                BigInteger bigInteger2 = new BigInteger(amount);
                if (isAdd || bigInteger1.compareTo(bigInteger2) >= 0) {
                    if (isAdd) {
                        user.setBalance(bigInteger1.add(bigInteger2).toString());
                    } else {
                        user.setBalance(bigInteger1.subtract(bigInteger2).toString());
                    }

                    //因为不是Web环境，所以mybatisplus的自动填充会失效，这里手动填充
                    user.setUpdateTime(LocalDateTime.now());
                    user.setUpdateBy(user.getId());
                    sysUserMapper.updateById(user);
                    //修改用户缓存
                    redissonClient.getBucket(RedisName.USER_BY_ID + userId).set(user);
                } else {
                    return false;
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        } finally {
            // 只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unLock: " + Thread.currentThread().getId());
                lock.unlock();
            }
        }
        return true;
    }

    @Override
    public boolean removeByIds(List<Long> ids) {
        int i = sysUserMapper.deleteBatchIds(ids);
        if (i > 0) {
            //删除用户缓存
            ids.forEach(id -> {
                if (id == 1L || id == 2L) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "系统账号不可删除");
                }
                redissonClient.getBucket(RedisName.USER_BY_ID + id).delete();
            });
            return true;
        }
        return false;
    }

    @Override
    public void updateById(SysUser user) {
        sysUserMapper.updateById(user);
        //修改用户缓存
        redissonClient.getBucket(RedisName.USER_BY_ID + user.getId()).set(user);
    }

    @Override
    public SysUser getByAccessKey(String accessKey) {
        RBucket<Long> bucket = redissonClient.getBucket(RedisName.USER_ID_BY_ACCESS_KEY + accessKey);
        if (bucket.isExists()) {
            return this.getById(bucket.get());
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("access_key", accessKey);
        SysUser user = sysUserMapper.selectOne(queryWrapper);
        bucket.set(user.getId());
        return user;
    }
}
