package com.fusheng.api_backend.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import com.fusheng.api_backend.common.BaseResponse;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.utils.FileUploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/file")
@RestController
@Tag(name = "文件操作")
@SaCheckLogin
public class FileController {
    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public BaseResponse<String> upload(MultipartFile file) {
        if (file == null) return BaseResponse.error(ErrorCode.PARAMS_ERROR, "文件不能为空");
        return BaseResponse.success(FileUploadUtil.uploadFile(file));
    }
}
