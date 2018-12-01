package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.*;
import com.balance.entity.user.UserArticleCollection;
import com.balance.mapper.shop.GoodsSpuMapper;
import com.balance.service.common.AliOSSBusiness;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class GoodsSpuService extends BaseService {

    @Autowired
    private AliOSSBusiness aliOSSBusiness;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsSpuMapper goodsSpuMapper;


    /**
     * 新增商品基本信息
     *
     * @param goodsSpu
     * @param goodsDefaultFile 商品默认图片
     * @param goodsDetailFiles 商品详情图片
     */
    public void createGoodsSpu(GoodsSpu goodsSpu, MultipartFile goodsDefaultFile, MultipartFile[] goodsDetailFiles) {
        String fileDirectory = DateFormatUtils.format(new Date(), "yyyy-MM-dd|HH");

        //上传文件
        String defaultImgUrl = aliOSSBusiness.uploadCommonPic(goodsDefaultFile, fileDirectory);
        goodsSpu.setDefaultImgUrl(defaultImgUrl);
        insert(goodsSpu);

        List<GoodsImg> goodsDetailImgList = new ArrayList<>();
        for (MultipartFile goodsDetailFile : goodsDetailFiles) {
            String detailImgUrl = aliOSSBusiness.uploadCommonPic(goodsDetailFile, fileDirectory);
            GoodsImg goodsDetailImg = new GoodsImg(goodsSpu.getId(), detailImgUrl, ShopConst.GOODS_IMG_TYPE_DETAIL);
            goodsDetailImgList.add(goodsDetailImg);
        }
        insertBatch(goodsDetailImgList, false);

    }

    /**
     * 商品基本信息列表
     *
     * @param goodsName  商品名称
     * @param categoryId 类目id
     * @param brandId    品牌id
     * @param supType    商品类型
     * @param status     商品状态
     * @param pagination 分页实体
     * @return
     */
    public List<GoodsSpu> listGoodsSpu(String goodsName, String categoryId, String brandId, Integer supType, Integer status, Pagination pagination) {
        List<GoodsSpu> goodsSpuList = goodsSpuMapper.listGoodsSpu(goodsName, categoryId, brandId, supType, status, pagination);
        return goodsSpuList;
    }

    /**
     * 商品详细信息
     *
     * @param spuId
     * @return
     */
    public GoodsDetail getGoodsDetail(String userId, String spuId) {
        GoodsDetail goodsDetail = new GoodsDetail();

        GoodsSpu goodsSpu = selectOneById(spuId, GoodsSpu.class);
        if (goodsSpu == null || !goodsSpu.getIsValid()) {
            throw new BusinessException("商品不存在");
        }

        //检查用户是否有收藏商品
        Map<String, Object> whereMap_ = ImmutableMap.of(GoodsCollection.User_id + "=", userId, GoodsCollection.Spu_id + "=", spuId);
        GoodsCollection goodsCollection = selectOneByWhereMap(whereMap_, GoodsCollection.class);
        if (goodsCollection != null) {
            goodsDetail.setIsCollect(1);
        } else {
            goodsDetail.setIsCollect(0);
        }

        //查询spu的sku列表
        List<GoodsSku> goodsSkuList = selectListByWhereString(GoodsSku.Spu_id + " = ", spuId, null, GoodsSku.class);

        //key:specName
        Map<String, List<GoodsSkuSelectValue>> skuListMap = new HashMap<>();

        List<GoodsSpec> goodsSpecList = new ArrayList<>();
        //1.获取商品所有的sku信息
        //2.按商品skuJson key进行分类
        //3.构造默认选中sku信息,商品图片信息
        for (GoodsSku goodsSku : goodsSkuList) {
            List<String> skuList = JSONObject.parseArray(goodsSku.getSpecJson(), String.class);
            for (String skuArrayStr : skuList) {
                String[] skuArray = skuArrayStr.split(":");
                String specNameId = skuArray[0];//规格名id
                String specValueId = skuArray[1];//规格值id

                String specName = goodsSpecService.getGoodSpecNameById(specNameId).getSpecName();
                String specValue = goodsSpecService.getGoodsSpecValueById(specValueId).getSpecValue();

                GoodsSkuSelectValue goodsSkuSelectValue = new GoodsSkuSelectValue(specNameId, specValueId, specValue);
                List<GoodsSkuSelectValue> goodsSkuSelectValueList;

                //如果规格名(key)于skuListMap已存在,则更新值
                if (skuListMap.containsKey(specName)) {
                    goodsSkuSelectValueList = skuListMap.get(specName);
                } else {//如果不存在，新建List
                    goodsSkuSelectValueList = new ArrayList<>();
                }

                goodsSkuSelectValueList.add(goodsSkuSelectValue);
                skuListMap.put(specName, goodsSkuSelectValueList);
            }
        }

        //skuListMap转换成GoodsSpecList
        for (Map.Entry<String, List<GoodsSkuSelectValue>> entry : skuListMap.entrySet()) {
            goodsSpecList.add(new GoodsSpec(entry.getKey(), entry.getValue()));
        }
        goodsDetail.setGoodsSpecList(goodsSpecList);

        //查询spu的sku对应图片
        Map<String, Object> whereMap = ImmutableMap.of(GoodsImg.Spu_id + " = ", spuId, GoodsImg.Sku_id + " = ", goodsSkuList.get(0).getId());
        List<GoodsImg> goodsImgList = selectListByWhereMap(whereMap, null, GoodsImg.class);
        List<String> introduceImgList = new ArrayList<>(5);
        List<String> detailImgList = new ArrayList<>(5);
        for (GoodsImg goodsImg : goodsImgList) {
            if (ShopConst.GOODS_IMG_TYPE_INTRODUCE == goodsImg.getImgType()) {//介绍图
                introduceImgList.add(goodsImg.getImgUrl());
            } else {//详情图
                detailImgList.add(goodsImg.getImgUrl());
            }
        }
        goodsDetail.setIntroduceImgList(introduceImgList);
        goodsDetail.setDetailImgList(detailImgList);

        return goodsDetail;
    }


    /**
     * 收藏商品
     *
     * @param userId
     * @param spuId
     */
    public void updateGoodCollection(String userId, String spuId) {
        GoodsSpu goodsSpu = selectOneById(spuId, GoodsSpu.class);
        ValueCheckUtils.notEmpty(goodsSpu, "未找到商品");

        Map<String, Object> whereMap = ImmutableMap.of(GoodsCollection.User_id + "=", userId, GoodsCollection.Spu_id + "=", spuId);
        GoodsCollection goodsCollection = selectOneByWhereMap(whereMap, GoodsCollection.class);

        if (goodsSpu == null) {//收藏商品
            goodsCollection = new GoodsCollection(userId, spuId, goodsSpu.getGoodsName(), goodsSpu.getLowPrice(), goodsSpu.getDefaultImgUrl());
            Integer i = insertIfNotNull(goodsCollection);
            if (i == 0) {
                throw new BusinessException("收藏商品失败");
            }
        } else { //取消收藏商品
            Integer i = delete(goodsCollection);
            if (i == 0) {
                throw new BusinessException("取消收藏失败");
            }
        }
    }

    /**
     * 用户的收藏商品列表
     *
     * @param userId
     * @param pagination
     * @return
     */
    public List<GoodsCollection> listGoodsCollection(String userId, Pagination pagination) {
        return selectListByWhereString(GoodsCollection.User_id, userId, pagination, GoodsCollection.class);
    }
}
