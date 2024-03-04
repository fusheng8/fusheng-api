package com.fusheng.api_backend.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import com.fusheng.api_backend.mapper.ApiInfoMapper;
import com.fusheng.api_backend.service.ApiInfoService;
import com.fusheng.api_backend.utils.FileUploadUtil;
import com.fusheng.common.constant.RedisKey;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoPageQueryDTO;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoSavaOrUpdateDTO;
import com.fusheng.common.model.dto.ApiInfo.ApiInfoUpdateSdkDTO;
import com.fusheng.common.model.entity.ApiInfo;
import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ApiInfoServiceImpl implements ApiInfoService {
    @Resource
    private ApiInfoMapper apiInfoMapper;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private Configuration templateConfiguration;

    @Value("${fusheng.gateway-url}")
    private String gatewayUrl;

    @Override
    public Page<ApiInfo> pageQuery(ApiInfoPageQueryDTO dto) {

        Page<ApiInfo> queryPage = new Page<>(dto.getCurrent(), dto.getPageSize());
        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            queryWrapper.like("name", dto.getName());
        }
        if (dto.getStatus() != null) {
            queryWrapper.eq("status", dto.getStatus());
        }
        if (dto.getMethod() != null) {
            queryWrapper.eq("method", dto.getMethod());
        }
        if (dto.getMethod() != null) {
            queryWrapper.eq("method", dto.getMethod());
        }
        if (dto.getUserId() != null) {
            queryWrapper.eq("user_id", dto.getUserId());
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
        return apiInfoMapper.selectPage(queryPage, queryWrapper);
    }

    @Override
    public boolean saveOrUpdate(ApiInfoSavaOrUpdateDTO dto) {
        ApiInfo apiInfo = new ApiInfo();
        BeanUtils.copyProperties(dto, apiInfo);
        apiInfo.setUserId(StpUtil.getLoginIdAsLong());
        if (dto.getResponseParams() != null)
            apiInfo.setResponseParams(new Gson().toJson(dto.getResponseParams()));
        else apiInfo.setResponseParams(null);

        if (dto.getRequestHeader() != null)
            apiInfo.setRequestHeader(new Gson().toJson(dto.getRequestHeader()));
        else apiInfo.setRequestHeader(null);

        if (dto.getRequestParams() != null)
            apiInfo.setRequestParams(new Gson().toJson(dto.getRequestParams()));
        else apiInfo.setRequestParams(null);

        if (dto.getId() == null) {
            return apiInfoMapper.insert(apiInfo) > 0;
        } else {
            int i = apiInfoMapper.updateById(apiInfo);
            if (i > 0) {
                redissonClient.getBucket(RedisKey.API_INFO_BY_ID + dto.getId()).set(apiInfo);
                return true;
            }
            return false;
        }
    }

    @Override
    public ApiInfo getById(Long id) {
        ApiInfo apiInfo = apiInfoMapper.selectById(id);
        if (apiInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        redissonClient.getBucket(RedisKey.API_INFO_BY_ID + id).set(apiInfo);
        return apiInfo;
    }

    @Override
    public boolean removeByIds(List<Long> ids) {
        int i = apiInfoMapper.deleteBatchIds(ids);
        if (i > 0) {
            ids.forEach(id -> {
                redissonClient.getBucket(RedisKey.API_INFO_BY_ID + id).delete();
            });
            return true;
        }
        return false;
    }

    @Override
    public ApiInfo getByMappingUrl(String mappingUrl) {
        RBucket<Long> bucket = redissonClient.getBucket(RedisKey.API_INFO_ID_BY_MAPPING_URL + mappingUrl);
        if (bucket.isExists()) {
            return this.getById(bucket.get());
        }
        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mapping_url", mappingUrl);
        ApiInfo apiInfo = apiInfoMapper.selectOne(queryWrapper);
        bucket.set(apiInfo.getId(), 60, TimeUnit.MINUTES);
        return apiInfo;
    }

    @Override
    public List<ApiInfo> queryByIds(List<Long> ids) {
        return apiInfoMapper.selectBatchIds(ids);
    }

    @Override
    public Boolean reviewApi(Long id, Integer status) {
        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setId(id);
        apiInfo.setStatus(status);
        int i = apiInfoMapper.updateById(apiInfo);
        if (i > 0) {
            redissonClient.getBucket(RedisKey.API_INFO_BY_ID + id).delete();
            return true;
        }
        return false;
    }

    @Override
    synchronized public String generateSdk(Long id) {
        //获取接口信息
        ApiInfo apiInfo = this.getById(id);
        String sdkPath = System.getProperty("user.dir") + "/sdk/";
        try {
            //清空原目录
            FileUtil.clean(sdkPath);
            //开始复制文件组成项目
            //获取资源目录
            String sdkResourceFilePath = getClass().getClassLoader().getResource("templates/static/api-sdk/").getPath();
            if (sdkResourceFilePath == null) throw new BusinessException(ErrorCode.OPERATION_ERROR);
            //复制目录
            FileUtil.copy(sdkResourceFilePath, sdkPath, true);

            //生成模板文件
            Template template = templateConfiguration.getTemplate("FushengApiClient.ftl");
            Map<String, Object> model = new HashMap<>();
            model.put("url", gatewayUrl);
            model.put("method", apiInfo.getMethod());
            BufferedWriter out = new FileWriter(sdkPath + "api-sdk/src/main/java/com/fusheng/api_client/FushengApiClient.java").getWriter(false);
            template.process(model, out);
            out.close();

            //打包
            mavenToPackage(sdkPath + "api-sdk");

            //上传打出来的jar包
            //jar包路径
            String jarPath = sdkPath + "api-sdk/target/fusheng-api-client.jar";
            return FileUploadUtil.uploadFile(new File(jarPath));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateSdk(ApiInfoUpdateSdkDTO dto) {
        ApiInfoSavaOrUpdateDTO apiInfoSavaOrUpdateDTO = new ApiInfoSavaOrUpdateDTO();
        apiInfoSavaOrUpdateDTO.setId(dto.getId());
        apiInfoSavaOrUpdateDTO.setSdk(new Gson().toJson(dto.getSdk()));
        this.saveOrUpdate(apiInfoSavaOrUpdateDTO);
    }

    /**
     * 调用maven打包得到jar
     *
     * @param src
     */
    private void mavenToPackage(String src) throws IOException, InterruptedException {
        // 清理之前的构建并打包
        // 注意不同操作系统，执行的命令不同
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";

        //判断当前操作是不是Windows
        String osName = System.getProperty("os.name").toLowerCase();
        String mavenCommand = osName.contains("windows") ? winMavenCommand : otherMavenCommand;

        // 这里一定要拆分！
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(src));

        Process process = processBuilder.start();

        // 读取命令的输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待命令执行完成
        int exitCode = process.waitFor();
        System.out.println("打包完成，退出码：" + exitCode);
    }
}
