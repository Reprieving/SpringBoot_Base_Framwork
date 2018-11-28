package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.shop.GoodsSku;
import com.balance.entity.shop.ShoppingCart;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartService extends BaseService {


    @Autowired
    private GoodsSpecService goodsSpecService;

    /**
     * 添加商品到购物车
     *
     * @param userId        用户id
     * @param spuId         spuid
     * @param specIdStrList 规格id jsonList 格式为 List 元素格式为"规格名id:规格值id"
     * @param number        购买数量
     */
    public void createShoppingItem(String userId, String spuId, List<String> specIdStrList, Integer number) {
        //2.计算订单总价格
        Map<String, Object> skuWhereMap = ImmutableMap.of(GoodsSku.Spu_id + " = ", spuId, GoodsSku.Spec_json + " = ", JSONObject.toJSONString(specIdStrList));
        GoodsSku goodsSku = selectOneByWhereMap(skuWhereMap, GoodsSku.class);
        if (goodsSku == null) {
            throw new BusinessException("未找到商品");
        }

        String specIdJson = JSONObject.toJSONString(specIdStrList);
        String specValueJson = goodsSpecService.strSpecIdToSpecValue(JSONObject.toJSONString(specIdStrList));

        ShoppingCart shoppingCart = new ShoppingCart(userId, spuId, goodsSku.getId(), specIdJson, specValueJson, number, goodsSku.getPrice());

        insertIfNotNull(shoppingCart);
    }


}
