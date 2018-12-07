package com.balance.controller.admin;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.shop.GoodsSpu;
import com.balance.service.shop.GoodsSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/goods")
public class AdminGoodsController {

    @Autowired
    private GoodsSpuService goodsSpuService;

    /**
     * 查询商品
     *
     * @return
     */
    @RequestMapping("spu/{operatorType}")
    public Result<?> spuList(GoodsSpu goodsSpu, @PathVariable Integer operatorType) {
        Object object = goodsSpuService.operator(goodsSpu, operatorType,goodsSpu.getDefaultImgUrl(),goodsSpu.getDetailImgs(),goodsSpu.getPagination());
        return ResultUtils.success(object);
    }


}
