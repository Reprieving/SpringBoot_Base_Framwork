package com.balance.service.information;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.InformationConst;
import com.balance.constance.MissionConst;
import com.balance.entity.information.Article;
import com.balance.entity.mission.Mission;
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

    /**
     * 根据类型查询发布文章
     *
     * @param pagination
     * @param announceType InformationConst.ARTICLE_TYPE_*
     * @return
     */
    public List<Article> listArticle(Pagination pagination, Integer announceType) {
        Map<String, Object> whereMap = ImmutableMap.of(Article.Article_type + "=", announceType);
        return selectListByWhereMap(whereMap, pagination, Article.class);
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
}
