package com.balance.service.shop;

import com.balance.architecture.service.BaseService;
import com.balance.entity.shop.GoodsSpecName;
import com.balance.entity.shop.GoodsSpecValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsSpecService {
    @Autowired
    private BaseService baseService;

    /**
     * 新增商品规格名字
     * @param goodSpec
     */
    public void createGoodsSpec(GoodsSpecName goodSpec){
        baseService.insert(goodSpec);
    }


    /**
     * 新增商品规格值
     * @param goodsSpecValue
     */
    public void createGoodsSpecValue(GoodsSpecValue goodsSpecValue){
        baseService.insert(goodsSpecValue);
    }

    /**
     * 查询商品规格名字
     * @param name 规格名字
     * @return
     */
    public List<GoodsSpecName> listGoodsSpecName(String name,Class<GoodsSpecName> tClazz){
        if(StringUtils.isNoneBlank(name)){
            return baseService.selectAll(null,tClazz);
        }else{
            return baseService.selectListByWhereString("spec_name = ",name,null,tClazz);
        }
    }

    /**
     * 查询商品规则值
     * @param specId 规格id
     * @return
     */
    public List<GoodsSpecValue> listGoodsSpecValue(String specId,Class<GoodsSpecValue> tClazz){
        return baseService.selectListByWhereString("spec_id = ",specId,null,tClazz);
    }
}
