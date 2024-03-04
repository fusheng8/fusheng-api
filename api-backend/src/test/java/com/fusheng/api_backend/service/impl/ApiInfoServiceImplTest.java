package com.fusheng.api_backend.service.impl;

import freemarker.template.Configuration;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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