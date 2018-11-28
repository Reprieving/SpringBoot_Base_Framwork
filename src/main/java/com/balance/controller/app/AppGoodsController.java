package com.balance.controller.app;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.shop.GoodsSpu;
import com.balance.service.shop.GoodsSpecService;
import com.balance.service.shop.GoodsSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("app/goods")
public class AppGoodsController {

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsSpuService goodsSpuService;

    /**
     * 查询商品
     * @param request
     * @param goodsSpu
     * @return
     */
    @RequestMapping("spuList")
    public Result<?> spuList(HttpServletRequest request, @RequestBody GoodsSpu goodsSpu, Pagination pagination){


        return ResultUtils.success("");
    }


}
