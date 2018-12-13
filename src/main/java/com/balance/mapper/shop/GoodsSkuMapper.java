package com.balance.mapper.shop;

import com.balance.entity.shop.GoodsSku;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsSkuMapper {

    /**
     * 删除sku的图片
     * @param spuId
     * @param skuId
     * @return
     */
    Integer deleteSkuImg(@Param("spuId") String spuId, @Param("skuId")String skuId);

    /**
     * sku详情
     * @param skuId
     * @return
     */
    GoodsSku getGoodsSku(String skuId);
}
