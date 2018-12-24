package com.balance.service.information;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.MissionConst;
import com.balance.entity.information.Investigation;
import com.balance.entity.mission.Mission;
import com.balance.entity.shop.OrderInfo;
import com.balance.entity.shop.OrderItem;
import com.balance.mapper.information.InvestigationMapper;
import com.balance.service.mission.MissionCompleteService;
import com.balance.service.mission.MissionService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

@Service
public class InvestigationService extends BaseService{

    @Autowired
    private MissionService missionService;

    @Autowired
    private MissionCompleteService missionCompleteService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private InvestigationMapper investigationMapper;



    /**
     * 按订单id查询调查模板
     * @param orderId
     * @return
     */
    public Investigation getInvestigationTemplate(String orderId, String userId){
        String spuId = checkInvestigation(orderId, userId);
        ImmutableMap<String, Object> whereMap = ImmutableMap.of(Investigation.User_id + "=", userId, Investigation.Order_id + "=", orderId);
        Investigation investigation = selectOneByWhereMap(whereMap, Investigation.class);
        if (investigation == null) {
            // 如果不为空 就是已经提交过了 可以看提交后问卷
            ImmutableMap<String, Object> whereMap2 = ImmutableMap.of(Investigation.Template_id + "=", "0", Investigation.Beauty_id + "=", spuId);
            investigation = selectOneByWhereMap(whereMap2, Investigation.class);
        }
        return investigation;
    }

    /**
     * 检查是否可以 提交问卷
     * @param orderId
     * @param userId
     * @return
     */
    private String checkInvestigation (String orderId, String userId) {
        OrderInfo orderInfo = selectOneByWhereString(OrderInfo.Id + " = ", orderId, OrderInfo.class);
        if (orderInfo == null || !orderInfo.getUserId().equals(userId) || orderInfo.getIfInvestigation()) {
            // 订单为空, 或者订单不是 该登录用户
            throw new BusinessException("数据状态异常");
        }
        List<OrderItem> orderItems = selectListByWhereString(OrderItem.Order_id + " = ", orderId, null, OrderItem.class);
        if (orderItems == null && orderItems.size() != 1) {
            // 订单项为空, 或者订单项不是一个
            throw new BusinessException("数据状态异常");
        }
        OrderItem orderItem = orderItems.get(0);
        return orderItem.getGoodsSpuId();
    }

    /**
     * 提交问卷调查
     * @param userId
     * @param investigation
     */
    public void createInvestigation(String userId, Investigation investigation) {
        String orderId = investigation.getOrderId();
        ValueCheckUtils.notEmpty(orderId, "订单号不能为空");
        investigation.setBeautyId(checkInvestigation(orderId, userId));
        ImmutableMap<String, Object> whereMap = ImmutableMap.of(Investigation.User_id + "=", userId, Investigation.Order_id + "=", orderId);
        if (selectOneByWhereMap(whereMap, Investigation.class) != null) {
            throw new BusinessException("该订单已经提交过问卷");
        }
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                int result1 = insertIfNotNull(investigation);
                OrderInfo orderInfo = new OrderInfo();
                orderInfo.setId(orderId);
                orderInfo.setIfInvestigation(true);
                int result2 = updateIfNotNull(orderInfo);
                if (result1 < 1 || result2 < 1) {
                    throw new BusinessException("提交问卷失败");
                }
                //完成任务
                
            }
        });

    }

    public Pagination getByPage(Map<String, Object> params) {
        Object templateId = params.get("templateId");
        List<Investigation> investigationList;
        int total;
        if ("0".equals(templateId)) {
            investigationList = investigationMapper.selectPageWithGoods(params);
            total = investigationMapper.selectCountWithGoods(params);
        } else {
            investigationList = investigationMapper.selectPageWithUser(params);
            total = investigationMapper.selectCountWithUser(params);
        }
        Pagination pagination = new Pagination();
        pagination.setObjectList(investigationList);
        pagination.setTotalRecordNumber(total);
        return pagination;
    }
}
