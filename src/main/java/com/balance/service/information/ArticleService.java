package com.balance.service.information;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.client.RedisClient;
import com.balance.constance.InformationConst;
import com.balance.constance.MissionConst;
import com.balance.constance.RedisKeyConst;
import com.balance.entity.information.Article;
import com.balance.entity.mission.Mission;
import com.balance.entity.sys.Subscriber;
import com.balance.entity.user.UserArticleCollection;
import com.balance.mapper.information.ArticleMapper;
import com.balance.service.mission.MissionCompleteService;
import com.balance.service.mission.MissionService;
import com.google.common.collect.ImmutableMap;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class ArticleService extends BaseService {

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionCompleteService missionCompleteService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 发布文章
     * @param userId
     * @param article
     */
    public void createArticle(String userId,Article article){
        if(InformationConst.ARTICLE_TYPE_ANNOUNCE == article.getArticleType() || InformationConst.ARTICLE_TYPE_ACTIVITY == article.getArticleType()){
            Subscriber subscriber = selectOneById(userId,Subscriber.class);
            ValueCheckUtils.notEmpty(subscriber,"非管理员用户不能发布公告和活动");
        }
        article.setCreateBy(userId);
        Integer i = insertIfNotNull(article);
        if(i == 0){
            throw new BusinessException("发布文章失败");
        }
    }

    /**
     * 根据类型查询文章
     *
     * @param pagination
     * @param articleType InformationConst.ARTICLE_TYPE_*
     * @return
     */
    public List<Article> listArticle(String userId, Integer articleType,Pagination pagination) {
        Map<String, Object> whereMap = ImmutableMap.of(Article.Article_type + "=", articleType);
        List<Article> articles =  selectListByWhereMap(whereMap, pagination, Article.class);
        articles.stream().forEach(article -> {
            String articleId = article.getId();
            article.setIfLike((Integer) redisClient.getHashKey(userId, RedisKeyConst.buildUserIdLikeArticleId(userId, articleId)));//是否点赞
            article.setLikeAmount((Integer) redisClient.getHashKey(userId, RedisKeyConst.buildArticleIdLikeCount(articleId)));//点赞数
            article.setIfCollect((Integer) redisClient.getHashKey(userId, RedisKeyConst.buildUserIdCollectArticleId(userId, articleId))); //是否收藏
        });
        return articles;
    }

    /**
     * 用户文章收藏列表
     *
     * @param userId
     * @param pagination
     * @return
     */
    public List<UserArticleCollection> listArticleCollection(String userId, Pagination pagination) {
        Map<String, Object> whereMap = ImmutableMap.of(UserArticleCollection.User_id + "=", userId);
        List<UserArticleCollection> collections = selectListByWhereMap(whereMap, pagination, UserArticleCollection.class);
        for (UserArticleCollection userArticleCollection : collections) {
            String articleId = userArticleCollection.getArticleId();
            Integer ifLike = (Integer) redisClient.getHashKey(userId, RedisKeyConst.buildUserIdLikeArticleId(userId, articleId));
            Integer likeAmount = (Integer) redisClient.getHashKey(userId, RedisKeyConst.buildArticleIdLikeCount(articleId));
            Integer ifCollect = (Integer) redisClient.getHashKey(userId, RedisKeyConst.buildUserIdCollectArticleId(userId, articleId));

            userArticleCollection.setIfLike(ifLike == null ? 0 : ifLike);//是否点赞
            userArticleCollection.setLikeAmount(likeAmount == null ? 0 : likeAmount);//点赞数
            userArticleCollection.setIfCollect(ifCollect == null ? 0 : ifCollect); //是否收藏
        }
        return collections;
    }

    /**
     * 根据id查询文章
     * @param articleId
     * @return
     */
    public Article getArticleById(String articleId){
        return selectOneById(articleId,Article.class);
    }

    /**
     * 收藏文章/取消收藏文章
     *
     * @param articleId 文章id
     * @param userId    用户id
     */
    public void updateCollection(String userId, String articleId) {
        Article article = selectOneById(articleId, Article.class);
        ValueCheckUtils.notEmpty(article, "未找到文章");

//        Map<String, Object> whereMap = ImmutableMap.of(UserArticleCollection.User_id + "=", userId, UserArticleCollection.Article_id + "=", articleId);
//        UserArticleCollection userArticleCollection = selectOneByWhereMap(whereMap, UserArticleCollection.class);
//        if (userArticleCollection == null) {//收藏文章
//            userArticleCollection = new UserArticleCollection(userId, article.getId(), article.getArticle_title(), article.getArticleType());
//            Integer i = insertIfNotNull(userArticleCollection);
//            if (i == 0) {
//                throw new BusinessException("收藏失败");
//            }
//        } else { //取消收藏文章
//            Integer i = delete(userArticleCollection);
//            if (i == 0) {
//                throw new BusinessException("取消收藏失败");
//            }
//        }

        String userIdCollectArticleIdKey = RedisKeyConst.buildUserIdCollectArticleId(userId, articleId);
        Integer i = (Integer) redisClient.getHashKey(userId, userIdCollectArticleIdKey);
        i = i == null ? 0 : i;     // i为空 则未收藏
        Integer j = i == 0 ? 1 : 0; //i == 0 设置为收藏, 为1则取消收藏
        redisClient.put(userId, userIdCollectArticleIdKey, j);
    }

    /**
     * 点赞文章/取消点赞文章
     *
     * @param userId    用户id
     * @param articleId 文章id
     */
    public void updateLike(String userId, String articleId) {
        Article article = selectOneById(articleId, Article.class);
        ValueCheckUtils.notEmpty(article, "未找到文章");
        String userIdLikeArticleIdKey = RedisKeyConst.buildUserIdLikeArticleId(userId, articleId);
        Integer i = (Integer) redisClient.getHashKey(userId, userIdLikeArticleIdKey);
        i = i == null ? 0 : i;// i为空 则未收藏
        Integer j = i == 0 ? 1 : 0; //i为0 设置为收藏, 为1则取消收藏
        redisClient.put(userId, userIdLikeArticleIdKey, j);
    }

    /**
     * 审核文章
     *
     * @param articleId
     * @param vertifyStatus
     */
    public void updateArticleVertify(String articleId, Integer vertifyStatus) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                String userId = selectOneById(articleId, Article.class).getCreateBy();

                Article article = new Article();
                article.setId(articleId);
                article.setVerifyStatus(vertifyStatus);
                Integer i = updateIfNotNull(article);
                if (i == 0) {
                    throw new BusinessException("审核失败");
                }

                if (vertifyStatus == InformationConst.ARTICLE_VERTIFY_STATUS_PASS) {
                    Mission mission = missionService.filterTaskByCode(MissionConst.RELEASE_ARTICLE, missionService.selectAll(null, Mission.class));
                    missionCompleteService.createOrUpdateMissionComplete(userId, mission);
                }
            }
        });
    }

    public Pagination getByPage4Admin(Map<String, Object> params) {
        Pagination pagination = new Pagination();
        pagination.setObjectList(articleMapper.selectByPage4Admin(params));
        pagination.setTotalRecordNumber(articleMapper.selectCount4Admin(params));
        return pagination;
    }

    public Pagination getByPage4User(Map<String, Object> params) {
        Pagination pagination = new Pagination();
        pagination.setObjectList(articleMapper.selectByPage4User(params));
        pagination.setTotalRecordNumber(articleMapper.selectCount4User(params));
        return pagination;
    }
}
