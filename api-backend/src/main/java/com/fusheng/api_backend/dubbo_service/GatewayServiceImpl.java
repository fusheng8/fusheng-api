package com.fusheng.api_backend.dubbo_service;

import com.fusheng.GatewayService;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.service.ApiInfoService;
import com.fusheng.api_backend.service.RequestLogsService;
import com.fusheng.api_backend.service.SysUserService;
import com.fusheng.common.model.entity.ApiInfo;
import com.fusheng.common.model.entity.KVPair;
import com.fusheng.common.model.entity.RequestLogs;
import com.fusheng.common.model.entity.SysUser;
import jakarta.annotation.Resource;
import javafx.util.Pair;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DubboService
@Transactional(rollbackFor = Exception.class)
public class GatewayServiceImpl implements GatewayService {
    @Resource
    private SysUserService userService;
    @Resource
    private ApiInfoService apiInfoService;
    @Resource
    private RequestLogsService requestLogsService;

    /**
     * 根据accessKey获取用户信息
     *
     * @param accessKey
     * @return
     */
    @Override
    public SysUser getUserByAccessKey(String accessKey) {
        //如果缓存中没有，就从数据库中查找
        SysUser user = userService.getByAccessKey(accessKey);
        return user;
    }

    /**
     * 根据mappingUrl获取api信息
     *
     * @param mappingUrl
     * @return
     */
    @Override
    public ApiInfo getApiInfoByMappingUrl(String mappingUrl) {
        return apiInfoService.getByMappingUrl(mappingUrl);
    }

    /**
     * 获取所有api信息
     *
     * @return
     */
    @Override
    public List<ApiInfo> getAllApiInfo() {
        return apiInfoService.getAllList();
    }

    /**
     * 改变用户余额
     *
     * @param userId
     * @param apiInfo
     * @return boolean 是否扣除成功
     */
    @Override
    public KVPair<Boolean, String> changeUserBalance(long userId, ApiInfo apiInfo) {
        String amount = apiInfo.getReduceBalance();
        Pair<Boolean, String> b1 = userService.deductUserBalance(userId, false, amount);
        if (!b1.getKey()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "扣除失败");
        }
        Pair<Boolean, String> b2 = userService.deductUserBalance(apiInfo.getUserId(), true, amount);
        if (!b2.getKey()) {
            userService.deductUserBalance(userId, true, amount);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "扣除失败");
        }
        return new KVPair<>(true, "扣除成功");
    }

    /**
     * 记录请求日志
     *
     * @param requestLogs
     * @return
     */
    @Override
    public void saveRequestLogs(RequestLogs requestLogs) {
        requestLogsService.save(requestLogs);
    }


}
