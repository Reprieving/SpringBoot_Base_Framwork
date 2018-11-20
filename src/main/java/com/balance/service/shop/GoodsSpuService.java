package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Pagination;
import com.balance.architecture.service.BaseService;
import com.balance.entity.shop.GoodsDetail;
import com.balance.entity.shop.GoodsImg;
import com.balance.entity.shop.GoodsSku;
import com.balance.entity.shop.GoodsSpu;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GoodsSpuService {

    @Autowired
    private BaseService baseService;

    /**
     * 新增商品信息
     *
     * @param goodsSpu     商品spu信息
     * @param goodsSkuList 商品sku信息
     */
    public void createGoodsSpu(GoodsSpu goodsSpu, List<GoodsSku> goodsSkuList) {
        String spuId = goodsSpu.getId();

        baseService.insert(goodsSpu);
        for (GoodsSku goodsSku : goodsSkuList) {
            List<Map> skuList = JSONObject.parseArray(goodsSku.getSpecJson(), Map.class);
            goodsSku.setSpuId(spuId);
        }
        if (goodsSkuList != null) {

            baseService.insertBatch(goodsSkuList, false);
        }

        for (GoodsSku goodsSku : goodsSkuList) {
            List<GoodsImg> goodsImgList = goodsSku.getGoodsImgList();
            for (GoodsImg goodsImg : goodsImgList) {
                goodsImg.setSpuId(spuId);
                goodsImg.setSkuId(goodsSku.getId());
            }
            baseService.insertBatch(goodsImgList, false);
        }

    }

    /**
     * 商品基本信息列表
     *
     * @param name
     * @param categoryId
     * @param brandId
     * @param pagination
     * @return
     */
    public List<GoodsSpu> listGoodsSpu(String name, String categoryId, String brandId, Pagination pagination) {
        Map<String, Object> whereMap = ImmutableMap.of("goods_name = ", name, "category_id = ", categoryId, "brand_id = ", brandId);
        return baseService.selectListByWhereMap(whereMap, GoodsSpu.class, pagination);

    }

    /**
     * 商品详细信息
     *
     * @param spuId
     * @return
     */
    public GoodsDetail listGoodsSku(String spuId, Pagination pagination) {
        List<GoodsSku> skuList = baseService.selectListByWhereString("spu_id = ", spuId, pagination, GoodsSku.class);
        //1.获取商品所有的sku信息
        //2.按商品skuJson key进行分类
        //3.构造默认选中sku信息,商品图片信息



        return null;
    }

}
