package com.balance.service.shop;

import com.balance.architecture.service.BaseService;
import com.balance.entity.shop.GoodsSku;
import com.balance.entity.shop.GoodsSpu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsSpuService {

    @Autowired
    private BaseService baseService;

    /**
     * 新增商品信息
     * @param goodsSpu 商品spu信息
     * @param goodsSkuList 商品sku信息
     */
    public void createGoodsSpu(GoodsSpu goodsSpu, List<GoodsSku> goodsSkuList){
        baseService.insert(goodsSpu);
        if(goodsSkuList !=null){
            baseService.insertBatch(goodsSkuList,true);
        }
    }


}
