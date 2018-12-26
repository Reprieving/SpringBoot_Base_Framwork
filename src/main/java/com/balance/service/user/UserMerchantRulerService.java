package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.CommonConst;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.GoodsBrand;
import com.balance.entity.user.UserMerchantRuler;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserMerchantRulerService extends BaseService{


    public Object operatorUserMerchantRuler(UserMerchantRuler userMerchantRuler, Integer operatorType) {
        Object o = null;
        switch (operatorType) {
            case CommonConst.OPERATOR_TYPE_INSERT: //添加
                o = "创建商户等级规则成功";
                if (save(userMerchantRuler) == 0) {
                    throw new BusinessException("创建商户等级规则失败");
                }
                break;

            case CommonConst.OPERATOR_TYPE_DELETE: //删除
                userMerchantRuler.setIfValid(false);
                o = "删除商户等级规则成功";
                if (updateIfNotNull(userMerchantRuler) == 0) {
                    throw new BusinessException("删除商户等级规则失败");
                }
                break;

            case CommonConst.OPERATOR_TYPE_QUERY_LIST: //查询列表
                o = list(userMerchantRuler, userMerchantRuler.getPagination());
                break;

            case CommonConst.OPERATOR_TYPE_QUERY_DETAIL: //详情
                o = detail(userMerchantRuler.getId());
                break;
        }
        return o;
    }

    public Integer save(UserMerchantRuler userMerchantRuler) {
        ValueCheckUtils.notEmpty(userMerchantRuler.getMachineRankName(), "商户等级名称不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getMachineStartCount(), "小样机器数量开始值不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getMachineEndCount(), "小样机器数量结束值不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getContractYearTimes(), "签约年数不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getComputeRewardRate(), "颜值奖励百分比不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getBeautyServiceProfit(), "线上领取小样分红不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getBecomeMemberProfit(), "办理礼盒年分红不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getComputeReturnRate(), "线上颜值返现不能为空");
        ValueCheckUtils.notEmpty(userMerchantRuler.getInviteProfitRate(), "邀请人分红百分比不能为空");

        String entityId = userMerchantRuler.getId();
        if (StringUtils.isNoneBlank(entityId)) {
            ValueCheckUtils.notEmpty(selectOneById(entityId, UserMerchantRuler.class), "未找到商户规则记录");
            return updateIfNotNull(userMerchantRuler);
        } else {
            return insertIfNotNull(userMerchantRuler);
        }
    }


    public List list(UserMerchantRuler userMerchantRuler, Pagination pagination) {
        Class clazz = UserMerchantRuler.class;
        String machineRankName = userMerchantRuler.getMachineRankName();
        Map<String, Object> whereMap;
        if (StringUtils.isNoneBlank(machineRankName)) {
            whereMap = ImmutableMap.of(
                    UserMerchantRuler.Machine_rank_name + " LIKE ", "%" + machineRankName + "%",
                    UserMerchantRuler.If_valid + "=", true);
        } else {
            whereMap = ImmutableMap.of(
                    UserMerchantRuler.If_valid + "=", true);
        }
        return selectListByWhereMap(whereMap, pagination, clazz);
    }

    public UserMerchantRuler detail(String entity) {
        return selectOneById(entity, UserMerchantRuler.class);
    }
}
