package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.GoodsImg;
import com.balance.entity.shop.GoodsSku;
import com.balance.mapper.shop.GoodsSkuMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsSkuService extends BaseService {
    @Autowired
    private GoodsSkuMapper goodsSkuMapper;

    @Autowired
    private GoodsImgService goodsImgService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    /**
     * 保存商品sku
     *
     * @param goodsSku         商品sku
     * @param introduceImgUrls 介绍图
     * @param detailImgUrls    详情图
     */
    public Integer saveGoodsSku(GoodsSku goodsSku, List<String> introduceImgUrls, List<String> detailImgUrls) {
        List<String> skuList = JSONObject.parseArray(goodsSku.getSpecJson(), String.class); //规格详情
        ValueCheckUtils.notEmpty(skuList, "商品规格不能为空");
        ValueCheckUtils.notEmpty(goodsSku.getPrice(), "商品单价不能为空");
        if (goodsSku.getStock() < 0 || goodsSku.getStock() == null) {
            throw new BusinessException("库存不能为空或少于0");
        }
        ValueCheckUtils.notEmpty(goodsSku.getSpuId(), "商品spuId不能为空");
        ValueCheckUtils.notEmpty(introduceImgUrls, "商品介绍图片不能为空");
        ValueCheckUtils.notEmpty(detailImgUrls, "商品详情图片不能为空");

        String skuId = goodsSku.getId();
        Integer a;
        if (StringUtils.isNoneBlank(skuId)) {
            ValueCheckUtils.notEmpty(selectOneById(skuId, GoodsSku.class), "未找到商品属性记录");
            GoodsSku goodsSkuParam = new GoodsSku();
            BeanUtils.copyProperties(goodsSku, goodsSkuParam, GoodsSku.Sku_no, GoodsSku.Create_time, GoodsSku.Status, GoodsSku.If_valid);
            a = updateIfNotNull(goodsSkuParam);
            a = goodsSkuMapper.deleteSkuImg(goodsSku.getSpuId(), skuId);
        } else {
            //新增商品
            String skuNo = System.currentTimeMillis() + "" + (int) ((Math.random() * 9 + 1) * 1000);
            goodsSku.setSkuNo(skuNo);
            a = insertIfNotNull(goodsSku);
        }

        a = goodsImgService.createGoodsImg(goodsSku.getSpuId(), goodsSku.getId(), introduceImgUrls, detailImgUrls);

        return a;
    }

    /**
     * 获取指定spu的sku列表
     *
     * @param spuId
     * @return
     */
    public List<GoodsSku> listGoodsSku(String spuId) {
        List<GoodsSku> goodsSkuList = selectListByWhereString(GoodsSku.Spu_id + "=", spuId, null, GoodsSku.class);
        goodsSkuList.forEach(goodsSku -> goodsSku.setSpecJson(goodsSpecService.strSpecIdToSpecValue(goodsSku.getSpecJson())));
        return goodsSkuList;
    }

    /**
     * sku详情
     *
     * @param skuId
     * @return
     */
    public GoodsSku getGoodsSku(String skuId) {
        GoodsSku goodsSku = goodsSkuMapper.getGoodsSku(skuId);
        goodsSku.setSpecJsonMap(new HashMap<>());
        List<String> specJsonArr = JSONObject.parseArray(goodsSku.getSpecJson(),String.class);
        specJsonArr.forEach(s -> {
            String[] strings = s.split(":");
            goodsSku.getSpecJsonMap().put(strings[0],strings[0]+":"+strings[1]);
        });
        goodsImgService.buildImgList(goodsSku);
        return goodsSku;
    }

    /**
     * 选择sku
     *
     * @param spuId    spuId
     * @param specJson 规格json字符串
     * @return
     */
    public GoodsSku chooseGoodsSku(String spuId, String specJson) {
        Map<String, Object> skuWhereMap = ImmutableMap.of(GoodsSku.Spu_id + " = ", spuId, GoodsSku.Spec_json + " = ", specJson);
        GoodsSku goodsSku = selectOneByWhereMap(skuWhereMap, GoodsSku.class);
        ValueCheckUtils.notEmpty(goodsSku, "未找到改商品信息");

        Map<String, Object> imgWhereMap = ImmutableMap.of(GoodsImg.Spu_id + " = ", spuId, GoodsImg.Sku_id + " = ", goodsSku.getId());
        List<GoodsImg> goodsImgList = selectListByWhereMap(imgWhereMap, null, GoodsImg.class);
        List<String> imgList = new ArrayList<>(goodsImgList.size());

        goodsImgList.stream().forEach(goodsImg -> imgList.add(goodsImg.getImgUrl()));
        goodsSku.setImgUrl(imgList);
        return goodsSku;
    }

    /**
     * 增删改查Spu
     *
     * @param goodsSku        sku实体
     * @param operatorType    操作类型
     * @param introduceImgUrl 介绍图URL列表
     * @param detailImgUrl    详情图URL列表
     * @return
     */
    public Object operator(GoodsSku goodsSku, Integer operatorType, List<String> introduceImgUrl, List<String> detailImgUrl) {
        Object o = null;
        switch (operatorType) {
            case ShopConst.OPERATOR_TYPE_INSERT: //添加
                String msgType = goodsSku.getId() == null ? "上传" : "更新";
                o = msgType + "商品属性成功";
                if (saveGoodsSku(goodsSku, introduceImgUrl, detailImgUrl) == 0) {
                    throw new BusinessException(msgType + "商品属性失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_DELETE: //删除
                GoodsSku delGoodsSku = new GoodsSku();
                delGoodsSku.setId(goodsSku.getId());
                delGoodsSku.setIfValid(false);
                o = "删除商品属性成功";
                if (updateIfNotNull(delGoodsSku) == 0) {
                    throw new BusinessException("删除商品属性失败");
                }
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_LIST: //查询列表
                o = listGoodsSku(goodsSku.getSpuId());
                break;

            case ShopConst.OPERATOR_TYPE_QUERY_DETAIL: //查询单个
                o = getGoodsSku(goodsSku.getId());
                break;
        }
        return o;
    }
}
