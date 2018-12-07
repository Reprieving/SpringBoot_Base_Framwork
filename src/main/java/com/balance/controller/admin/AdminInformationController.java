package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.constance.InformationConst;
import com.balance.entity.information.Article;
import com.balance.service.information.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 资讯文章
 */
@RestController
@RequestMapping("admin/information")
public class AdminInformationController {

    @Autowired
    private ArticleService articleService;


    @PostMapping("list")
    public Result<?> articleList(@RequestBody Map<String, Object> params) {
        Object type = params.get("type");
//        if(InformationConst.ARTICLE_TYPE_ANNOUNCE == Article.getArticleType() || InformationConst.ARTICLE_TYPE_ACTIVITY == Article.getArticleType()){
//
//        }
        Pagination pagination = null;
//        if (type == 1) {
//            pagination = articleService.getByPage4Admin(params);
//        } else {
            pagination = articleService.getByPage4User(params);
//        }
        return ResultUtils.success(pagination.getObjectList(), pagination.getTotalRecordNumber());
    }



}
