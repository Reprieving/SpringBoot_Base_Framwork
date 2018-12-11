package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.shop.*;
import com.balance.service.shop.GoodsSpecService;
import com.balance.service.shop.GoodsSpuService;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("admin/goods")
public class AdminGoodsController {

    @Autowired
    private GoodsSpuService goodsSpuService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    /**
     * 查询商品
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
     * @param goodsSku
     * @return
     */
    @RequestMapping("sku/operator/{operatorType}")
    public Result<?> skuOperator(@RequestBody GoodsSku goodsSku,@PathVariable String operatorType){
        Object o = null;


        return ResultUtils.success(o);
    }
}
