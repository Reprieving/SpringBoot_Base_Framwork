package com.balance.service.shop;

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
    public void createGoodsSpu(GoodsSpu goodsSpu, List<GoodsSku> goodsSkuList, List<GoodsImg> goodsImgList) {
        baseService.insert(goodsSpu);
        if (goodsSkuList != null) {
            baseService.insertBatch(goodsSkuList, true);
        }

    }

    /**
     * 查询商品信息列表
     */
    public List<GoodsSpu> listGoodsSpu(String name, String categoryId, String brandId, Pagination pagination) {
        Map<String, Object> whereMap = ImmutableMap.of("goods_name = ", name, "category_id = ", categoryId, "brand_id = ", brandId);
        return baseService.selectListByWhereMap(whereMap, GoodsSpu.class, pagination);

    }

    public List<GoodsDetail> listGoodsSku(String spuId) {
        return null;
    }

}
