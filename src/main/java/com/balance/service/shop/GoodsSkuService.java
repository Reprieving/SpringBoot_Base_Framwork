package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.GoodsImg;
import com.balance.entity.shop.GoodsSku;
import com.balance.service.common.AliOSSBusiness;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GoodsSkuService extends BaseService {
    @Autowired
    private AliOSSBusiness aliOSSBusiness;

    /**
     * 新增商品sku
     *
     * @param goodsSku            商品sku
     * @param goodsIntroduceFiles
     */
    public void createGoodsSku(GoodsSku goodsSku, MultipartFile[] goodsIntroduceFiles) {
        String fileDirectory = DateFormatUtils.format(new Date(), "yyyy-MM-dd|HH");
        insert(goodsSku);
        List<GoodsImg> goodsIntroduceImgList = new ArrayList<>();
        for (MultipartFile goodsIntroduceFile : goodsIntroduceFiles) {
            String detailImgUrl = aliOSSBusiness.uploadCommonPic(goodsIntroduceFile, fileDirectory);
            GoodsImg goodsIntroduceImg = new GoodsImg(goodsSku.getSpuId(), detailImgUrl, ShopConst.GOODS_IMG_TYPE_INTRODUCE);
            goodsIntroduceImgList.add(goodsIntroduceImg);
        }
        insertBatch(goodsIntroduceImgList, false);
    }

    /**
     * 选择sku
     *
     * @param spuId spuId
     * @param specJson 规格json字符串
     * @return
     */
    public GoodsSku chooseGoodsSku(String spuId, String specJson) {
        Map<String, Object> skuWhereMap = ImmutableMap.of(GoodsSku.Spu_id + " = ", spuId, GoodsSku.Spec_json + " = ", specJson);
        GoodsSku goodsSku = selectOneByWhereMap(skuWhereMap, GoodsSku.class);
        ValueCheckUtils.notEmpty(goodsSku,"未找到改商品信息");

        Map<String, Object> imgWhereMap = ImmutableMap.of(GoodsImg.Spu_id + " = ", spuId, GoodsImg.Sku_id + " = ", goodsSku.getId());
        List<GoodsImg> goodsImgList = selectListByWhereMap(imgWhereMap, null, GoodsImg.class);
        List<String> imgList = new ArrayList<>(goodsImgList.size());

        goodsImgList.stream().forEach(goodsImg -> imgList.add(goodsImg.getImgUrl()));

        goodsSku.setImgUrl(imgList);

        return goodsSku;
    }

}
