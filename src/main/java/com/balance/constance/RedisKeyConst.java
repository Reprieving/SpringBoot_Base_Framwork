package com.balance.constance;

import com.balance.entity.user.UserArticleCollection;

public class RedisKeyConst {
    public static final String COMPUTE_POWER_RANK_NO = "computePowerRankNo";//用户算力排行榜名次key
    public static final String COMPUTE_POWER_RANK_LIST = "computePowerRankList";//用户算力排行榜名次key

    public static final String ARTICLE_IF_LIKE = "articleIfLike";//用户是否点赞文章
    public static final String USERID_LIKE_ARTICLEID = UserArticleCollection.User_id + ":like:" + UserArticleCollection.Article_id;//用户id点赞文章id标志
    public static String buildUserIdLikeArticleId(String userId,String articleId){
        return USERID_LIKE_ARTICLEID.replace(UserArticleCollection.User_id,userId).replace(UserArticleCollection.Article_id,articleId);
    }

    public static final String ARTICLE_LIKE_COUNT = "articleLikeCount";//文章点赞数
    public static final String ARTICLEID_LIKE_COUNT = UserArticleCollection.Article_id+":LikeCount";//文章id点赞数
    public static String buildArticleIdLikeCount(String articleId){
        return ARTICLEID_LIKE_COUNT.replace(UserArticleCollection.Article_id,articleId);
    }

    public static final String ARTICLE_IF_COLLECT = "articleIfCollect";//用户是否收藏文章
    public static final String USERID_COLLECT_ARTICLEID = UserArticleCollection.User_id + ":collect:" + UserArticleCollection.Article_id;//用户id收藏文章id标志
    public static String buildUserIdCollectArticleId(String userId,String articleId){
        return USERID_COLLECT_ARTICLEID.replace(UserArticleCollection.User_id,userId).replace(UserArticleCollection.Article_id,articleId);
    }
}
