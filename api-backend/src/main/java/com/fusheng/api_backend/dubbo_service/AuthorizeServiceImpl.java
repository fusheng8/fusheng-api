package com.fusheng.api_backend.dubbo_service;

import com.fusheng.AuthorizeService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class AuthorizeServiceImpl implements AuthorizeService {
    @Override
    public String sayHello(String name) {
        return "Hello, " + name ;
    }
}
