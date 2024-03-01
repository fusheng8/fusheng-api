package com.fusheng.api_backend.service.impl;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;

class ApiInfoServiceImplTest {

    @Resource
    Configuration configuration;

    @Test
    void generateSdk() throws IOException {
        URL sdkFileUrl = getClass().getClassLoader().getResource("templates/static/sdk");
//        System.out.println(apiClientConfigFile.getPath());

    }
}