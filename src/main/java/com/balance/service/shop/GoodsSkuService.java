package com.balance.service.shop;

import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.service.BaseService;
import com.balance.constance.ShopConst;
import com.balance.entity.shop.GoodsImg;
import com.balance.entity.shop.GoodsSku;
import com.balance.service.common.AliOSSBusiness;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GoodsSkuService extends BaseService{
    @Autowired
    private AliOSSBusiness aliOSSBusiness;

    /**
     * 新增商品sku
     * @param goodsSku 商品sku
     * @param goodsIntroduceFiles
     */
    public void createGoodsSku(GoodsSku goodsSku, MultipartFile[] goodsIntroduceFiles){
        String fileDirectory = DateFormatUtils.format(new Date(),"yyyy-MM-dd|HH");
        List<String> spceJsonList = JSONObject.parseArray(goodsSku.getSpecJson(),String.class);//用于校验前端传过来的spec组合值(格式=>["规格名:规格值,规格名:规格值"])
        insert(goodsSku);
        List<GoodsImg> goodsIntroduceImgList = new ArrayList<>();
        for(MultipartFile goodsIntroduceFile:goodsIntroduceFiles){
            String detailImgUrl = aliOSSBusiness.uploadCommonPic(goodsIntroduceFile,fileDirectory);
            GoodsImg goodsIntroduceImg = new GoodsImg(goodsSku.getSpuId(),detailImgUrl, ShopConst.GOODS_IMG_TYPE_INTRODUCE);
            goodsIntroduceImgList.add(goodsIntroduceImg);
        }
        insertBatch(goodsIntroduceImgList,false);
    }


}
