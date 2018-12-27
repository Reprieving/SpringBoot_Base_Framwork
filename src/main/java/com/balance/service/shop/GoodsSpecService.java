package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.utils.ValueCheckUtils;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.*;
import com.balance.mapper.shop.GoodsSpecMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsSpecService extends BaseService {

    @Autowired
    private GoodsSpecMapper goodsSpecMapper;

    /**
     * 新增商品规格名字
     *
     * @param goodsSpecName
     */
    public Integer saveGoodsSpecName(GoodsSpecName goodsSpecName) {
        ValueCheckUtils.notEmpty(goodsSpecName.getSpecName(), "规格名不能为空");
        String specNameId = goodsSpecName.getId();
        if(StringUtils.isNoneBlank(specNameId)){
            ValueCheckUtils.notEmpty(selectOneById(specNameId, GoodsSpecName.class), "未找到商品规格名记录");
            return updateIfNotNull(goodsSpecName);
        }else {
            return insertIfNotNull(goodsSpecName);
        }
//        Integer a;
//        a = insertIfNotNull(goodsSpecName);
//        List<GoodsSpecNameValue> goodsSpecNameValueList = new ArrayList<>();
//        goodsSpecName.getSpecValueIdList().forEach(specSpecValueId -> goodsSpecNameValueList.add(new GoodsSpecNameValue(goodsSpecName.getId(), specSpecValueId)));
//        a = insertBatch(goodsSpecNameValueList, false);
//        return a;

    }


    /**
     * 查询商品规格名字
     *
     * @param specName 规格名字
     * @return
     */
    public List<GoodsSpecName> listGoodsSpecName(String specName, Pagination pagination) {
        Class clazz = GoodsSpecName.class;
        if (StringUtils.isNoneBlank(specName)) {
            return selectListByWhereString(GoodsSpecName.Spec_name + " = ", specName, pagination, clazz);
        } else {
            return selectAll(pagination, clazz);
        }
    }

    /**
     * 查询商品规则值
     *
     * @param specId 规格id
     * @return
     */
    public List<GoodsSpecValue> listGoodsSpecValue(String specId) {
        return selectListByWhereString(GoodsSpecValue.Spec_id + " = ", specId, null, GoodsSpecValue.class);
    }

    /**
     * 规格名详情
     *
     * @param specNameId
     * @return
     */
    private GoodsSpecName getGoodsSpecName(String specNameId) {
        return goodsSpecMapper.getGoodsSpecName(specNameId);
    }

    /**
     * 查询商品规格名字实体
     *
     * @param specNameId
     * @return
     */
    public GoodsSpecName getGoodSpecNameById(String specNameId) {
        return selectOneById(specNameId, GoodsSpecName.class);
    }

    /**
     * 查询商品规格值实体
     */
    public GoodsSpecValue getGoodsSpecValueById(String specValueId) {
        return selectOneById(specValueId, GoodsSpecValue.class);
    }

    /**
     * 规格id json 翻译成 规格值Str ["规格id":"规格值id"] => "规格名:规格值 规格名:规格值"
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
     * 规格id List 翻译成 规格值Str ["规格id":"规格值id"] => "规格名:规格值 规格名:规格值"
     *
     * @param specIdStrList
     */
    public String strSpecIdToSpecValue(List<String> specIdStrList) {
        String specStr = "";
        String specName;
        String specValue;
        if(specIdStrList!=null){
            for (String specIdStr : specIdStrList) {
                String[] specIdArr = specIdStr.split(":");
                specName = getGoodSpecNameById(specIdArr[0]).getSpecName();
                specValue = getGoodsSpecValueById(specIdArr[1]).getSpecValue();
                if (specName != null && specValue != null) {
                    specStr += " " + specName + ":" + specValue;
                }
            }
        }
        return specStr;
    }

    /**
     * 规格名操作
     *
     * @param goodsSpecName
     * @param operatorType
     * @return
     */
    public Object operatorSpecName(GoodsSpecName goodsSpecName, Integer operatorType) {
        Object o = null;
        switch (operatorType) {
            case ShopConst.OPERATOR_TYPE_INSERT: //添加
                String msgType = goodsSpecName.getId() == null ? "创建" : "更新";
                o = msgType + "规格名成功";
                if (saveGoodsSpecName(goodsSpecName) == 0) {
                    throw new BusinessException(msgType + "规格名失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_DELETE: //删除
                o = "删除规格名成功";
                if (delete(goodsSpecName) == 0) {
                    o = "删除规格名失败";
                }
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_LIST: //查询列表
                o = listGoodsSpecName(goodsSpecName.getSpecName(), goodsSpecName.getPagination());
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_DETAIL: //详情
                o = getGoodsSpecName(goodsSpecName.getId());
                break;
        }
        return o;
    }

    /**
     * 获取所有规格名id
     * @return
     */
    public List<String> listAllSpecName(){
        List<GoodsSpecName> goodsSpecNames = selectAll(null,GoodsSpecName.class);
        List<String> ids = new ArrayList<>(goodsSpecNames.size());
        goodsSpecNames.forEach(e->ids.add(e.getId()));
        return ids;
    }

    /**
     * 新增商品规格值
     *
     * @param goodsSpecValue
     */
    public Integer saveGoodsSpecValue(GoodsSpecValue goodsSpecValue) {
        ValueCheckUtils.notEmpty(goodsSpecValue.getSpecValue(), "规格值不能为空");
        String specValueId = goodsSpecValue.getId();
        if(StringUtils.isNoneBlank(specValueId)){
            ValueCheckUtils.notEmpty(selectOneById(goodsSpecValue.getSpecId(), GoodsSpecName.class), "未找到商品规格名记录");
            ValueCheckUtils.notEmpty(selectOneById(specValueId, GoodsSpecValue.class), "未找到商品规格值记录");
            return updateIfNotNull(goodsSpecValue);
        }else {
            return insertIfNotNull(goodsSpecValue);
        }
    }

    /**
     * 规格值列表
     */
    public List listGoodsSpecValue(String specNameId, Pagination pagination) {
        Class clazz = GoodsSpecValue.class;
        if (StringUtils.isNoneBlank(specNameId)) {
            return selectListByWhereString(GoodsSpecValue.Spec_id + " = ", specNameId, pagination, clazz);
        } else {
            return selectAll(pagination, clazz);
        }
    }

    /**
     * 规格名详情
     */
    public GoodsSpecValue getGoodsSpecValue(String specValueId) {
        return selectOneById(specValueId, GoodsSpecValue.class);
    }

    /**
     * 规格值操作
     *
     * @param goodsSpecValue
     * @param operatorType
     * @return
     */
    public Object operatorSpecValue(GoodsSpecValue goodsSpecValue, Integer operatorType) {
        Object o = null;
        switch (operatorType) {
            case ShopConst.OPERATOR_TYPE_INSERT: //添加
                o = "创建规格名成功";
                if (saveGoodsSpecValue(goodsSpecValue) == 0) {
                    o = "创建规格名失败";
                }
                break;

            case ShopConst.OPERATOR_TYPE_DELETE: //删除
                o = "删除规格名成功";
                if (delete(goodsSpecValue) == 0) {
                    o = "删除规格名失败";
                }
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_LIST: //查询列表
                o = listGoodsSpecValue(goodsSpecValue.getSpecId(), goodsSpecValue.getPagination());
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_DETAIL: //详情
                o = getGoodsSpecValue(goodsSpecValue.getId());
                break;
        }
        return o;
    }



}
