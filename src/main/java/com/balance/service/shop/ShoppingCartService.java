package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.shop.GoodsSku;
import com.balance.entity.shop.ShoppingCart;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartService extends BaseService{


    @Autowired
    private GoodsSpecService goodsSpecService;

    /**
     * 添加商品到购物车
     * @param userId 用户id
     * @param spuId spuid
     * @param specNameValueStrList 规格id json 格式为 List 元素格式为"规格名id:规格值id"
     * @param number 购买数量
     */
    public void createShoppingItem(String userId,String spuId, List<String> specNameValueStrList,Integer number){
        //sku字符串转换
        Map<String, String> skuSpecMap = new HashMap<>(); //key:规格名id， value:规格值id
        for (String specNameValueStr : specNameValueStrList) { //specNameValueStr格式 = "规格名id:规格值id"
            String[] specArr = specNameValueStr.split(":");
            skuSpecMap.put(specArr[0], specArr[1]);
        }

        //2.计算订单总价格
        Map<String, Object> skuWhereMap = ImmutableMap.of("spu_id = ", spuId, "spec_json = ", JSONObject.toJSONString(skuSpecMap));
        GoodsSku goodsSku = selectOneByWhereMap(skuWhereMap, GoodsSku.class);
        if (goodsSku == null) {
            throw new BusinessException("未找到商品");
        }

        String specStr  = goodsSpecService.buildOrderItemSpecStr(JSONObject.toJSONString(skuSpecMap));

        ShoppingCart shoppingCart = new ShoppingCart(userId,spuId,goodsSku.getId(),specStr,number,goodsSku.getPrice());

        insertIfNotNull(shoppingCart);


    }
}
