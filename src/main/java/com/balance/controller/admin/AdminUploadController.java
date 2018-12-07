package com.balance.controller.admin;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.service.common.AliOSSBusiness;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("admin/upload")
public class AdminUploadController {

    @Autowired
    private AliOSSBusiness aliOSSBusiness;

    @RequestMapping("common")
    public Result<?> create(MultipartFile file) throws Exception {
        String fileDirectory = DateFormatUtils.format(new Date(), "yyyy-MM-dd|HH");
        String url = aliOSSBusiness.uploadCommonPic(file,fileDirectory);
        Map<String,Object> map = ImmutableMap.of("url",url);
        return ResultUtils.success(map);
    }
}
