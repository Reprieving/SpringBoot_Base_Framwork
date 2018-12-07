package com.balance.controller.app;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.controller.app.req.PaginationReq;
import com.balance.entity.information.Article;
import com.balance.entity.information.Investigation;
import com.balance.service.information.ArticleService;
import com.balance.service.information.InvestigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("app/information")
public class AppInformationController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private InvestigationService investigationService;

    /**
     * 按类型查询文章列表
     *
     * @param request
     * @param articleType
     * @return
     */
    @RequestMapping("article/list/{articleType}")
    public Result<?> articleList(HttpServletRequest request, @PathVariable Integer articleType, Pagination pagination) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(articleService.listArticle(userId, articleType, pagination));
    }

    /**
     * 发布文章
     *
     * @param request
     * @param article
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("article/create")
    public Result<?> createArticle(HttpServletRequest request, Article article) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        articleService.createArticle(userId, article);
        return ResultUtils.success();
    }

    /**
     * 收藏/取消收藏文章
     *
     * @param request
     * @param articleId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("article/collect/{articleId}")
    public Result<?> updateCollect(HttpServletRequest request, @PathVariable String articleId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        articleService.updateCollection(userId, articleId);
        return ResultUtils.success();
    }

    /**
     * 点赞/取消点赞文章
     *
     * @param request
     * @param articleId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("article/like/{articleId}")
    public Result<?> updateLike(HttpServletRequest request, @PathVariable String articleId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        articleService.updateLike(userId, articleId);
        return ResultUtils.success();
    }

    /**
     * 用户收藏文章列表
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("article/collection/list")
    public Result<?> collectionList(HttpServletRequest request, Pagination pagination) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(articleService.listArticleCollection(userId, pagination));
    }

    /**
     * 文章详情
     *
     * @param articleId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("article/{articleId}")
    public Result<?> getArticle(@PathVariable String articleId) {
        return ResultUtils.success(articleService.getArticleById(articleId));
    }

    /**
     * 按美妆品id查询调查模板
     *
     * @param request
     * @param beautyId
     * @return
     */
    @RequestMapping("investTemplate/list/{beautyId}")
    public Result<?> articleList(HttpServletRequest request, @PathVariable String beautyId, Pagination pagination) {
        return ResultUtils.success(investigationService.listInvestigationTemplate(beautyId, pagination));
    }

    /**
     * 提交调查
     *
     * @param request
     * @param investigation
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("invest/create")
    public Result<?> createInvestigation(HttpServletRequest request, Investigation investigation) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        investigationService.createInvestigation(userId, investigation);
        return ResultUtils.success();
    }


}
