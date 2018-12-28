package com.balance.controller.app;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import com.balance.controller.app.req.GoodsExchangeReq;
import com.balance.controller.app.req.GoodsScanReq;
import com.balance.controller.app.req.PaginationReq;
import com.balance.controller.app.req.ShopOrderSkuReq;
import com.balance.entity.shop.GoodsCollection;
import com.balance.entity.shop.GoodsDetail;
import com.balance.entity.shop.GoodsSku;
import com.balance.entity.shop.GoodsSpu;
import com.balance.service.shop.GoodsSkuService;
import com.balance.service.shop.GoodsSpecService;
import com.balance.service.shop.GoodsSpuService;
import com.balance.service.shop.OrderService;
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
    private GoodsSpuService goodsSpuService;

    @Autowired
    private GoodsSkuService goodsSkuService;

    @Autowired
    private OrderService orderService;

    /**
     * 查询商品
     *
     * @param goodsSpu
     * @return
     */
    @RequestMapping("spuList")
    public Result<?> spuList(GoodsSpu goodsSpu) {
        Pagination pagination = goodsSpu.buildPagination();
        List<GoodsSpu> goodsSpuList = goodsSpuService.listGoodsSpu(goodsSpu, goodsSpu.buildPagination());
        return ResultUtils.success(goodsSpuList,pagination.getTotalRecordNumber());
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
    public Result<?> skuChoose(ShopOrderSkuReq shopOrderSkuReq) {
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
     * @param pagination
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("collection/{spuType}")
    public Result<?> collection(HttpServletRequest request,@PathVariable Integer spuType,  Pagination pagination) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        List<GoodsCollection> goodsCollections = goodsSpuService.listGoodsCollection(userId, pagination,spuType);
        return ResultUtils.success(goodsCollections);
    }

    /**
     * 兑换小样礼包
     *
     * @param request
     * @param paramReq
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("beauty/exchange")
    public Result<?> exchangeBeauty(HttpServletRequest request, GoodsExchangeReq paramReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        orderService.exchangeSpuPackageOrder(userId, paramReq.getVoucherId(), paramReq.getSpuId(), paramReq.getAddressId());
        return ResultUtils.success();
    }

    /**
     * 扫码领取小样
     * @param request
     * @param paramReq
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("beauty/scan")
    public Result<?> scanBeauty(HttpServletRequest request, GoodsScanReq paramReq) throws UnsupportedEncodingException {
        String userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        orderService.scanBeautyOrder(userId, paramReq.getAisleCode(), paramReq.getMachineCode());
        return ResultUtils.success();
    }
}
