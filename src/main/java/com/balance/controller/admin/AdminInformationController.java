package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.information.Article;
import com.balance.service.information.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

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
        Pagination pagination = articleService.getByPage(params);
        return ResultUtils.success(pagination.getObjectList(), pagination.getTotalRecordNumber());
    }

    @GetMapping("get/{id}")
    public Result<?> get(@PathVariable String id) {
        return ResultUtils.success(articleService.selectOneById(id, Article.class));
    }

    @GetMapping("delete/{id}")
    public Result<?> delete(@PathVariable String id) {
        Article article = new Article();
        article.setId(id);
        int result = articleService.delete(article);
        if (result > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error("操作失败");
        }
    }


    @PostMapping("save/{id}")
    public Result<?> save(Article article, HttpServletRequest request, @PathVariable String id) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        int result = 0;
        if ("0".equals(id)) {
            article.setCreateBy(userId);
            result = articleService.insertIfNotNull(article);
        } else {
            result = articleService.updateIfNotNull(article);
        }
        if (result > 0) {
            return ResultUtils.success();
        } else {
            return ResultUtils.error("操作失败");
        }
    }

}
