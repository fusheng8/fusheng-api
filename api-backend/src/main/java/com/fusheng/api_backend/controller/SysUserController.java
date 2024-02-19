package com.fusheng.api_backend.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.api_backend.common.BaseResponse;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.service.BalanceOrderServie;
import com.fusheng.api_backend.service.SysRoleService;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.api_backend.service.WithdrawService;
import com.fusheng.common.model.dto.SysUser.*;
import com.fusheng.common.model.dto.withdraw.WithdrawDTO;
import com.fusheng.common.model.entity.BalanceOrder;
import com.fusheng.common.model.entity.SysUser;
import com.fusheng.common.model.entity.Withdraw;
import com.fusheng.common.model.vo.SysUser.SysUserInfoVO;
import com.fusheng.common.model.vo.SysUser.SysUserLoginVO;
import com.fusheng.common.model.vo.SysUser.SysUserPageQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import javafx.util.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.WatchService;
import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
@SaCheckLogin
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private BalanceOrderServie balanceOrderServie;
    @Resource
    private WithdrawService withdrawService;

    @SaIgnore
    @Operation(summary = "登录")
    @PostMapping("/login")
    public BaseResponse<SysUserLoginVO> login(@Validated @RequestBody SysUserLoginDTO sysUserLoginDTO) {
        return BaseResponse.success(sysUserService.login(sysUserLoginDTO));
    }

    @SaIgnore
    @Operation(summary = "注册")
    @PostMapping("/register")
    public BaseResponse register(@Validated @RequestBody SysUserRegisterDTO sysUserRegisterDTO) {
        sysUserService.register(sysUserRegisterDTO);
        return BaseResponse.success();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public BaseResponse<SysUserInfoVO> info() {
        long id = StpUtil.getLoginIdAsLong();
        SysUser user = sysUserService.getById(id);
        SysUserInfoVO sysUserInfoVO = new SysUserInfoVO();
        // 将角色信息转换为key
        BeanUtils.copyProperties(user, sysUserInfoVO);
        sysUserInfoVO.setRoles(sysRoleService.getRoleKeysByIds(user.getRoles()));
        return BaseResponse.success(sysUserInfoVO);
    }

    @SaCheckRole("admin")
    @Operation(summary = "查询用户列表")
    @PostMapping("/list")
    public BaseResponse<SysUserPageQueryVO> list(@RequestBody SysUserPageQueryDTO sysUserPageQueryDTO) {
        Page<SysUser> sysUserPage = sysUserService.pageQuery(sysUserPageQueryDTO);
        SysUserPageQueryVO vo = new SysUserPageQueryVO();
        vo.setList(sysUserPage.getRecords());
        vo.setTotal(sysUserPage.getTotal());
        vo.setCurrentPage(sysUserPage.getCurrent());
        vo.setPageSize(sysUserPage.getSize());
        return BaseResponse.success(vo);
    }


    @Operation(summary = "保存用户")
    @PostMapping("/save")
    public BaseResponse<SysUser> save(@RequestBody SysUserSaveDTO dto) {
        //权限校验 非管理员只能修改自己的信息
        if (!StpUtil.hasRole("admin") &&
                (dto.getId() != null && dto.getId() != StpUtil.getLoginIdAsLong())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        SysUser user = sysUserService.saveOrUpdate(dto, false);

        return BaseResponse.success(user);
    }

    @SaCheckRole("admin")
    @Operation(summary = "批量删除用户")
    @GetMapping("/deleteByIds")
    public BaseResponse<Boolean> deleteByIds(@RequestParam List<Long> ids) {
        boolean res = sysUserService.removeByIds(ids);
        return BaseResponse.success(res);
    }

    @SaCheckRole("admin")
    @Operation(summary = "根据用户id查找对应的角色id列表")
    @GetMapping("/getRoleIdsByUserId")
    public BaseResponse<String> getRoleIdsByUserId(@RequestParam Long userId) {
        SysUser user = sysUserService.getById(userId);
        if (user == null) throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        return BaseResponse.success(user.getRoles());
    }

    @SaCheckRole("admin")
    @Operation(summary = "设置用户角色")
    @PostMapping("/setUserRole")
    public BaseResponse setUserRole(@RequestBody SetUserRoleDTO setUserRoleDTO) {
        sysUserService.setUserRole(setUserRoleDTO);
        return BaseResponse.success();
    }

    @Operation(summary = "获取该用户拥有的角色key")
    @GetMapping("/getHasRoleKeys")
    public BaseResponse<List<String>> getHasRoleKeys() {
        long id = StpUtil.getLoginIdAsLong();
        SysUser user = sysUserService.getById(id);
        List<String> roleKeys = sysRoleService.getRoleKeysByIds(user.getRoles());
        return BaseResponse.success(roleKeys);
    }

    @Operation(summary = "重置SecretKey")
    @GetMapping("/resetSecretKey")
    public BaseResponse<String> resetSecretKey(@Validated @NotBlank String code) {
        long id = StpUtil.getLoginIdAsLong();
        SysUser user = sysUserService.getById(id);
        if (user == null) throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        return BaseResponse.success(sysUserService.resetSecretKey(user, code));
    }

    @Operation(summary = "注销")
    @GetMapping("/logout")
    public BaseResponse logout() {
        StpUtil.logout();
        return BaseResponse.success();
    }

    @Operation(summary = "重置密码")
    @PostMapping("/resetPassword")
    @SaIgnore
    public BaseResponse resetPassword(@RequestBody @Validated SysUserResetPasswordDTO sysUserResetPasswordDTO) {
        sysUserService.resetPassword(sysUserResetPasswordDTO);
        return BaseResponse.success();
    }

    @Operation(summary = "用户充值积分")
    @GetMapping("/payBalance")
    public BaseResponse<String> payBalance(@RequestParam
                                           @Validated
                                           @NotBlank(message = "充值积分不能为空")
                                           @Positive(message = "充值积分必须大于0")
                                           String count) {
        if (count.indexOf('.') != -1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "充值积分必须为整数");
        }

        // 创建充值订单
        ;
        long l = balanceOrderServie.payBalance(count, StpUtil.getLoginIdAsLong());
        return BaseResponse.success(Long.toString(l));
    }

    @Operation(summary = "获取充值界面")
    @GetMapping(value = "/payBalancePage", produces = "text/html")
    @SaIgnore
    public String getPayBalancePage(@RequestParam @Validated @NotBlank(message = "订单号不能为空") String orderId,
                                    @RequestParam @Validated @NotBlank(message = "返回地址不能为空") String returnUrl) {

        BalanceOrder balanceOrder = balanceOrderServie.getById(Long.parseLong(orderId));
        if (balanceOrder == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        return balanceOrderServie.getPayPage(balanceOrder, returnUrl);
    }

    @Operation(summary = "用户获取是否充值成功")
    @GetMapping("/payBalanceStatus")
    public BaseResponse<Integer> getPayBalancePage(@RequestParam @Validated @NotBlank(message = "订单号不能为空") String orderId) {
        BalanceOrder balanceOrder = balanceOrderServie.getById(Long.parseLong(orderId));
        //判断用户是否有权限
        if (balanceOrder.getUserId() != StpUtil.getLoginIdAsLong()) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return BaseResponse.success(balanceOrder.getState());
    }

    @Transactional
    @Operation(summary = "提现")
    @PostMapping("/withdrawBalance")
    @SaCheckLogin
    public BaseResponse withdrawBalance(@RequestBody @Validated WithdrawDTO dto) {
        BigDecimal bigDecimal = new BigDecimal(dto.getAmount());
        //小于100分不允许提现
        if (bigDecimal.compareTo(new BigDecimal("100")) < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提现积分必须大于100分");
        }
        //扣除手续费5%
        BigDecimal count = bigDecimal.multiply(new BigDecimal("0.0095"));
        Withdraw withdraw = new Withdraw();
        BeanUtils.copyProperties(dto, withdraw);
        withdraw.setUserId(StpUtil.getLoginIdAsLong());
        withdraw.setAmount(count.setScale(2, RoundingMode.HALF_UP).toString());
        withdraw.setStatus(1);

        // 扣除用户积分
        Pair<Boolean, String> pair = sysUserService.deductUserBalance(StpUtil.getLoginIdAsLong(), false, dto.getAmount());
        if (!pair.getKey()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, pair.getValue());
        }
        // 创建提现订单
        balanceOrderServie.insertWithdrawOrder(withdraw);
        withdrawService.withdrawBalance(withdraw);

        return BaseResponse.success();
    }


}
