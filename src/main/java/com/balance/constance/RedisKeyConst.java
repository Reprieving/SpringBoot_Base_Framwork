package com.balance.constance;

import com.balance.entity.shop.SampleMachineLocation;
import com.balance.entity.user.NearByUser;
import com.balance.entity.user.UserArticleCollection;

public class RedisKeyConst {
    public static final String COMPUTE_POWER_RANK_NO = "computePowerRankNo";//用户算力排行榜名次key
    public static final String COMPUTE_POWER_RANK_LIST = "computePowerRankList";//用户算力排行榜名次key

    //用户id点赞文章id标志
    public static final String USERID_LIKE_ARTICLEID = UserArticleCollection.User_id + ":like:" + UserArticleCollection.Article_id;
    public static String buildUserIdLikeArticleId(String userId, String articleId) {
        return USERID_LIKE_ARTICLEID.replace(UserArticleCollection.User_id, userId).replace(UserArticleCollection.Article_id, articleId);
    }

    //文章id点赞数
    public static final String ARTICLEID_LIKE_COUNT = UserArticleCollection.Article_id + ":LikeCount";
    public static String buildArticleIdLikeCount(String articleId) {
        return ARTICLEID_LIKE_COUNT.replace(UserArticleCollection.Article_id, articleId);
    }

    //用户id收藏文章id标志
    public static final String USERID_COLLECT_ARTICLEID = UserArticleCollection.User_id + ":collect:" + UserArticleCollection.Article_id;
    public static String buildUserIdCollectArticleId(String userId, String articleId) {
        return USERID_COLLECT_ARTICLEID.replace(UserArticleCollection.User_id, userId).replace(UserArticleCollection.Article_id, articleId);
    }

    //用户坐标
    public static final String USER_COORDINATE = "userCoordinate:" + NearByUser.User_id;
    public static String buildUserCoordinate(String userId, String provinceCode, String cityCode, String regionCode, String streetCode) {
        String newStr = USER_COORDINATE.replace(NearByUser.User_id, userId);
        newStr += "|" + provinceCode + cityCode + regionCode + streetCode;
        return newStr;
    }

    //用户偷取列表数目
    public static final String USER_STOLEN_COUNT = "userStealCount:"+ NearByUser.User_id;
    public static String buildUserStealCount(String userId) {
        return USER_STOLEN_COUNT.replace(NearByUser.User_id, userId);
    }

    //小样机器坐标
    public static final String SAMPLE_MACHINE_ID = "machineId:"+ SampleMachineLocation.CityCode;
    public static String buildSampleMachineId(String cityCode) {
        return SAMPLE_MACHINE_ID.replace(SampleMachineLocation.CityCode, cityCode);
    }

    //办理会员开关
    public static final String BECOME_MEMBER_FLAG = "become_member_flag:";

    //卡券兑换开关
    public static final String USE_VOUCHER_FLAG = "user_voucher_flag:";

    /** 用户每天银行卡提现数量 string 类型*/
    public static final String BANK_WITHDRAW_AMOUNT = "bank_withdraw_amount:";

    /** 用户IP信息记录 五分钟 strinig 类型*/
    public static final String USER_IP_INFO = "user:ipInfo:%s";

    /** 用户分享次数 string 类型*/
    public static final String USER_SHARE_TIME= "user:shareTime:%s";

}
