package com.balance.controller.admin;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.UUIDUtils;
import com.balance.utils.Base64DecodedMultipartFile;
import com.balance.architecture.utils.ResultUtils;
import com.balance.service.common.AliOSSBusiness;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("admin/upload")
public class AdminUploadController {

    @Autowired
    private AliOSSBusiness aliOSSBusiness;

    @RequestMapping("common")
    public Result<?> create(MultipartFile file) {
        String url = aliOSSBusiness.uploadCommonPic(file);
        Map<String, Object> map = ImmutableMap.of("url", url);
        return ResultUtils.success(map);
    }


    @PostMapping(value = "uploadBase64")
    public Result<?> base64UpLoad(String base64Data) {
        String dataPrix = "";
        String data = "";
        if (StringUtils.isBlank(base64Data)) {
            return ResultUtils.error("上传失败, 上传图片数据为空");
        } else {
            String[] d = base64Data.split(",");
            if (d != null && d.length == 2) {
                dataPrix = d[0];
                data = d[1];
            } else {
                return ResultUtils.error("上传失败, 数据不合法");
            }
        }
        if (!StringUtils.equalsAnyIgnoreCase(dataPrix, "data:image/jpeg;base64",
                "data:image/x-icon;base64", "data:image/gif;base64", "data:image/png;base64")) {
            return ResultUtils.error("上传图片格式不合法");
        }
        //因为BASE64Decoder的jar问题，此处使用spring框架提供的工具包
        byte[] bytes = Base64Utils.decodeFromString(data);
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }
        Base64DecodedMultipartFile base64DecodedMultipartFile = new Base64DecodedMultipartFile(bytes, dataPrix);
        return create(base64DecodedMultipartFile);
    }

    /**
     * 上传 APP 包
     */
    @PostMapping("uploadApp")
    public Result<?> uploadApp(MultipartFile multipartFile) {
        return ResultUtils.success(ImmutableMap.of("url", aliOSSBusiness.uploadAppClient(multipartFile)));
    }
}
