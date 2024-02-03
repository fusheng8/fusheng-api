package com.fusheng.api_backend.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fusheng.api_backend.common.ErrorCode;
import com.fusheng.api_backend.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class FileUploadUtil {
    private static final String FILE_URL = "https://fs-im-kefu.7moor-fs1.com/";
    private static final String FILE_UPLOAD_URL = "https://up.qiniu.com/";
    private static final String CREATE_FILE_URL = "https://up.ly93.cc/upload/token";
    public static String uploadFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Map<String, Object> param = getCredentials(fileName);
        File file = transferToFile(multipartFile);
        param.put("file",file );
        String res = HttpUtil.post(FILE_UPLOAD_URL, param);
        // 删除临时文件
        FileUtil.del(file);
        JSONObject entries = JSONUtil.parseObj(res);
        return FILE_URL + entries.getStr("key");
    }
    private static File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file=File.createTempFile(UUID.randomUUID().toString(), filename[1] + ".");
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    private static Map<String, Object> getCredentials(String fileName) {
        Map<String,Object> param=new HashMap<>();
        param.put("name",fileName);
        String res = HttpUtil.post(CREATE_FILE_URL, param);
        JSONObject entries = JSONUtil.parseObj(res);
        if (!entries.getStr("code").equals("0")) {
            log.error("获取文件上传凭证失败{}",res);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "文件上传失败");
        }
        JSONObject data = entries.getJSONObject("data");
        Map<String, Object> map = new HashMap<>();
        map.put("name", fileName);
        map.put("token", data.getStr("token"));
        map.put("key", data.getStr("key"));
        return map;
    }
}
