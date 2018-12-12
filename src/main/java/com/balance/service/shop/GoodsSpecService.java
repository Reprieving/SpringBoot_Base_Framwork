package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.service.BaseService;
import com.balance.entity.shop.GoodsSpec;
import com.balance.entity.shop.GoodsSpecName;
import com.balance.entity.shop.GoodsSpecValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GoodsSpecService {
    @Autowired
    private BaseService baseService;

    /**
     * 新增商品规格名字
     *
     * @param goodSpec
     */
    public void createGoodsSpec(GoodsSpecName goodSpec) {
        baseService.insert(goodSpec);
    }

    /**
     * 新增商品规格值
     *
     * @param goodsSpecValue
     */
    public void createGoodsSpecValue(GoodsSpecValue goodsSpecValue) {
        baseService.insert(goodsSpecValue);
    }

    /**
     * 查询商品规格名字
     *
     * @param name 规格名字
     * @return
     */
    public List<GoodsSpecName> listGoodsSpecName(String name, Class<GoodsSpecName> tClazz) {
        if (StringUtils.isNoneBlank(name)) {
            return baseService.selectAll(null, tClazz);
        } else {
            return baseService.selectListByWhereString(GoodsSpecName.Spec_name + " = ", name, null, tClazz);
        }
    }

    /**
     * 查询商品规则值
     *
     * @param specId 规格id
     * @return
     */
    public List<GoodsSpecValue> listGoodsSpecValue(String specId) {
        return baseService.selectListByWhereString(GoodsSpecValue.Spec_id + " = ", specId, null, GoodsSpecValue.class);
    }

    /**
     * 查询商品规格名字实体
     *
     * @param specNameId
     * @return
     */
    public GoodsSpecName getGoodSpecNameById(String specNameId) {
        return baseService.selectOneById(specNameId, GoodsSpecName.class);
    }

    /**
     * 查询商品规格值实体
     */
    public GoodsSpecValue getGoodsSpecValueById(String specValueId) {
        return baseService.selectOneById(specValueId, GoodsSpecValue.class);
    }

    /**
     * 规格id json 翻译成 规格值Str {"规格id":"规格值id"} => "规格名:规格值 规格名:规格值"
     *
     * @param specJson
     */
    public String strSpecIdToSpecValue(String specJson) {
        String specStr;
        List<String> specIdStrList = JSONObject.parseArray(specJson, String.class);
        specStr = strSpecIdToSpecValue(specIdStrList);
        return specStr;
    }

    /**
     * 规格id List 翻译成 规格值Str {"规格id":"规格值id"} => "规格名:规格值 规格名:规格值"
     *
     * @param specIdStrList
     */
    public String strSpecIdToSpecValue(List<String> specIdStrList) {
        String specStr = "";
        String specName;
        String specValue;
        for (String specIdStr : specIdStrList) {
            String[] specIdArr = specIdStr.split(":");
            specName = getGoodSpecNameById(specIdArr[0]).getSpecName();
            specValue = getGoodsSpecValueById(specIdArr[1]).getSpecValue();
            if (specName != null && specValue != null) {
                specStr += " " + specName + ":" + specValue;
            }
        }
        return specStr;
    }
}
