package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.shop.*;
import com.balance.service.shop.*;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/goods")
public class AdminGoodsController {

    @Autowired
    private GoodsSpuService goodsSpuService;

    @Autowired
    private GoodsSkuService goodsSkuService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private ShopInfoService shopInfoService;

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    @Autowired
    private GoodsBrandService goodsBrandService;

    /**
     * spu操作
     *
     * @return
     */
    @RequestMapping("spu/operator/{operatorType}")
    public Result<?> spuOperator(@RequestBody GoodsSpu goodsSpu, @PathVariable Integer operatorType) {
        Pagination pagination = goodsSpu.getPagination();
        Object object = goodsSpuService.operator(goodsSpu, operatorType, goodsSpu.getDefaultImgUrl(), goodsSpu.getIntroduceImgUrl(), goodsSpu.getDetailImgUrl(), pagination);
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }

        Integer count = pagination == null ? 0 : pagination.getTotalRecordNumber();
        return ResultUtils.success(object, count);
    }

    /**
     * 查询spu的规格列表
     *
     * @param spuId
     * @return
     */
    @RequestMapping("spu/{spuId}/specList")
    public Result<?> spuSpecList(@PathVariable String spuId) {
        List<GoodsSpecName> allSpecName = goodsSpuService.allSpecName();
        List<GoodsSpecName> specNameList = goodsSpuService.listSpecName(spuId);
        List<String> specNameIdList = new ArrayList<>();
        specNameList.stream().forEach(goodsSpecName -> specNameIdList.add(goodsSpecName.getId()));
        Map<String, List<?>> specNameMap = ImmutableMap.of("allSpecName", allSpecName, "specNameIdList", specNameIdList);
        return ResultUtils.success(specNameMap);
    }

    /**
     * 获取spu的specValue
     *
     * @param spuId
     * @return
     */
    @RequestMapping("spu/{spuId}/specSelectList")
    public Result<?> spuSpecSelectList(@PathVariable String spuId) {
        List<GoodsSpecName> specNameList = goodsSpuService.listSpecName(spuId);
        for (GoodsSpecName goodsSpecName : specNameList) {
            goodsSpecName.setGoodsSpecValueList(goodsSpecService.listGoodsSpecValue(goodsSpecName.getId()));
        }
        return ResultUtils.success(specNameList);
    }

    /**
     * 更新spu的specName
     *
     * @param goodsSpu
     * @return
     */
    @RequestMapping("spu/specNames/update")
    public Result<?> updateSpuSpecName(@RequestBody GoodsSpu goodsSpu) {
        goodsSpuService.updateSpuName(goodsSpu);
        return ResultUtils.success("更新规格成功");
    }

    /**
     * sku操作
     *
     * @param goodsSku
     * @return
     */
    @RequestMapping("sku/operator/{operatorType}")
    public Result<?> skuOperator(@RequestBody GoodsSku goodsSku, @PathVariable Integer operatorType) {
        Object object = goodsSkuService.operator(goodsSku, operatorType, goodsSku.getIntroduceImgUrl(), goodsSku.getDetailImgUrl());
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }
        return ResultUtils.success(object);
    }

    /**
     * 规格名操作
     *
     * @param goodsSpecName
     * @param operatorType
     * @return
     */
    @RequestMapping("specName/operator/{operatorType}")
    public Result<?> operatorSpecName(@RequestBody GoodsSpecName goodsSpecName, @PathVariable Integer operatorType) {
        Pagination pagination = goodsSpecName.getPagination();
        Object object = goodsSpecService.operatorSpecName(goodsSpecName, operatorType);
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }
        Integer count = pagination == null ? 0 : pagination.getTotalRecordNumber();
        return ResultUtils.success(object, count);
    }

    /**
     * 获取所有规格名id
     */
    @RequestMapping("specName/all")
    public Result<?> allSpecNameId() {
        return ResultUtils.success(goodsSpecService.listAllSpecName());
    }


    /**
     * 规格值操作
     *
     * @param goodsSpecValue
     * @param operatorType
     * @return
     */
    @RequestMapping("specValue/operator/{operatorType}")
    public Result<?> operatorSpecValue(@RequestBody GoodsSpecValue goodsSpecValue, @PathVariable Integer operatorType) {
        Pagination pagination = goodsSpecValue.getPagination();
        Object object = goodsSpecService.operatorSpecValue(goodsSpecValue, operatorType);
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }
        Integer count = pagination == null ? 0 : pagination.getTotalRecordNumber();
        return ResultUtils.success(object, count);
    }


    /**
     * 商铺信息操作
     *
     * @param shopInfo
     * @param operatorType
     * @return
     */
    @RequestMapping("shopInfo/operator/{operatorType}")
    public Result<?> operatorShopInfo(HttpServletRequest request, @RequestBody ShopInfo shopInfo, @PathVariable Integer operatorType) throws UnsupportedEncodingException {
        Pagination pagination = shopInfo.getPagination();
        String subscriberId = JwtUtils.getSubscriberByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        shopInfo.setSubscriberId(subscriberId);
        Object object = shopInfoService.operatorShopInfo(shopInfo, operatorType);
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }
        Integer count = pagination == null ? 0 : pagination.getTotalRecordNumber();
        return ResultUtils.success(object, count);
    }

    /**
     * 类目信息操作
     *
     * @param goodsCategory
     * @param operatorType
     * @return
     */
    @RequestMapping("category/operator/{operatorType}")
    public Result<?> operatorCategory(HttpServletRequest request, @RequestBody GoodsCategory goodsCategory, @PathVariable Integer operatorType) throws UnsupportedEncodingException {
        Pagination pagination = goodsCategory.getPagination();
        Object object = goodsCategoryService.operatorCategory(goodsCategory, operatorType);
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }
        Integer count = pagination == null ? 0 : pagination.getTotalRecordNumber();
        return ResultUtils.success(object, count);
    }

    /**
     * 品牌操作
     *
     * @param goodsBrand
     * @param operatorType
     * @return
     */
    @RequestMapping("brand/operator/{operatorType}")
    public Result<?> operatorBrand(HttpServletRequest request, @RequestBody GoodsBrand goodsBrand, @PathVariable Integer operatorType) throws UnsupportedEncodingException {
        Pagination pagination = goodsBrand.getPagination();
        String subscriberId = JwtUtils.getSubscriberByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        goodsBrand.setSubscriberId(subscriberId);
        Object object = goodsBrandService.operatorBrand(goodsBrand, operatorType);
        if (object instanceof String) {
            String message = String.valueOf(object);
            return ResultUtils.success(message);
        }
        Integer count = pagination == null ? 0 : pagination.getTotalRecordNumber();
        return ResultUtils.success(object, count);
    }


    @RequestMapping("spu/selectData")
    public Result<?> selectDataList(HttpServletRequest request) throws UnsupportedEncodingException {
        String subscriberId = JwtUtils.getSubscriberByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        return ResultUtils.success(goodsSpuService.listSelectData(subscriberId));
    }
}
