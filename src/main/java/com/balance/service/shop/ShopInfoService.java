package com.balance.service.shop;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.ShopInfo;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ShopInfoService extends BaseService {

    /**
     * 新增商铺
     *
     * @param shopInfo
     */
    public Integer saveShopInfo(ShopInfo shopInfo) {
        ValueCheckUtils.notEmpty(shopInfo.getShopName(), "商铺名不能为空");
        String shopInfoId = shopInfo.getId();
        if (StringUtils.isNoneBlank(shopInfoId)) {
            ValueCheckUtils.notEmpty(selectOneById(shopInfo.getId(), ShopInfo.class), "未找到商品规格名记录");
            return updateIfNotNull(shopInfo);
        } else {
            return insertIfNotNull(shopInfo);
        }
    }

    /**
     * 商铺列表
     *
     * @param shopInfo   商铺查询对象
     * @param pagination 分页对象
     * @return
     */
    public List listShopInfo(ShopInfo shopInfo, Pagination pagination) {
        Class clazz = ShopInfo.class;
        String shopName = shopInfo.getShopName();
        if (StringUtils.isNoneBlank(shopName)) {
            Map<String, Object> whereMap = ImmutableMap.of(
                    ShopInfo.Shop_name + " LIKE ", "%" + shopName + "%",
                    ShopInfo.Subscriber_id + "=", shopInfo.getSubscriberId(),
                    ShopInfo.If_valid + "=", true
            );
            return selectListByWhereMap(whereMap, pagination, clazz);
        } else {
            Map<String, Object> whereMap = ImmutableMap.of(ShopInfo.Subscriber_id + "=", shopInfo.getSubscriberId());
            return selectListByWhereMap(whereMap, pagination, clazz);
        }
    }


    /**
     * 商铺详情
     *
     * @param shopInfoId 商铺id
     * @return
     */
    public ShopInfo getShopInfo(String shopInfoId) {
        return selectOneById(shopInfoId, ShopInfo.class);
    }


    /**
     * 商铺信息操作
     *
     * @param shopInfo
     * @param operatorType
     * @return
     */
    public Object operatorShopInfo(ShopInfo shopInfo, Integer operatorType) {
        Object o = null;
        switch (operatorType) {
            case ShopConst.OPERATOR_TYPE_INSERT: //添加
                o = "创建商铺成功";
                if (saveShopInfo(shopInfo) == 0) {
                    throw new BusinessException("创建商铺失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_DELETE: //删除
                shopInfo.setIfValid(false);
                o = "删除商铺成功";
                if (updateIfNotNull(shopInfo) == 0) {
                    throw new BusinessException("删除商铺失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_LIST: //查询列表
                o = listShopInfo(shopInfo, shopInfo.getPagination());
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_DETAIL: //详情
                o = getShopInfo(shopInfo.getId());
                break;
        }
        return o;
    }
}
