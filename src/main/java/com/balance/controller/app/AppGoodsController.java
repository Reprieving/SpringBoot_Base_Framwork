package com.balance.controller.app;

import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.controller.app.req.PaginationReq;
import com.balance.controller.app.req.ShopOrderSkuReq;
import com.balance.entity.shop.GoodsCollection;
import com.balance.entity.shop.GoodsDetail;
import com.balance.entity.shop.GoodsSku;
import com.balance.entity.shop.GoodsSpu;
import com.balance.service.shop.GoodsSkuService;
import com.balance.service.shop.GoodsSpecService;
import com.balance.service.shop.GoodsSpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("app/goods")
public class AppGoodsController {

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsSpuService goodsSpuService;

    @Autowired
    private GoodsSkuService goodsSkuService;

    /**
     * 查询商品
     *
     * @param request
     * @param goodsSpu
     * @return
     */
    @RequestMapping("spuList")
    public Result<?> spuList(HttpServletRequest request, GoodsSpu goodsSpu) {
        List<GoodsSpu> goodsSpuList = goodsSpuService.listGoodsSpu(goodsSpu.getGoodsName(), goodsSpu.getCategoryId(), goodsSpu.getBrandId(),
                goodsSpu.getSpuType(), goodsSpu.getStatus(), goodsSpu.getPagination());
        return ResultUtils.success(goodsSpuList);
    }

    /**
     * 商品详情
     *
     * @param request
     * @param spuId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("spuDetail/{spuId}")
    public Result<?> spuDetail(HttpServletRequest request, @PathVariable String spuId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        GoodsDetail goodsDetail = goodsSpuService.getGoodsDetail(userId, spuId);
        return ResultUtils.success(goodsDetail);
    }

    /**
     * sku选择
     *
     * @param shopOrderSkuReq
     * @return
     */
    @RequestMapping("skuChoose")
    public Result<?> skuChoose(ShopOrderSkuReq shopOrderSkuReq){
        GoodsSku goodsSku = goodsSkuService.chooseGoodsSku(shopOrderSkuReq.getSpuId(), shopOrderSkuReq.getSpecIdStr());
        return ResultUtils.success(goodsSku);
    }

    /**
     * 收藏商品
     *
     * @param request
     * @param spuId
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("collect/{spuId}")
    public Result<?> collect(HttpServletRequest request, @PathVariable String spuId) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        goodsSpuService.updateGoodCollection(userId, spuId);
        return ResultUtils.success();
    }

    /**
     * 用户商品收藏列表
     *
     * @param request
     * @param paginationReq
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("collection")
    public Result<?> collection(HttpServletRequest request, PaginationReq paginationReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        List<GoodsCollection> goodsCollections = goodsSpuService.listGoodsCollection(userId, paginationReq.getPagination());
        return ResultUtils.success(goodsCollections);
    }
}
