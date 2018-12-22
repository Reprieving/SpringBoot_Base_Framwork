package com.balance.mapper.shop;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.shop.GoodsCollection;
import com.balance.entity.shop.GoodsSpecName;
import com.balance.entity.shop.GoodsSpu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GoodsSpuMapper {

    /**
     * 商品基本信息列表
     *
     * @param goodsName  商品名称
     * @param categoryId 类目id
     * @param brandId    品牌id
     * @param spuType    商品类型
     * @param status     商品状态
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param pagination 分页实体
     * @return
     */
    List<GoodsSpu> listGoodsSpu(@Param("goodsName") String goodsName, @Param("categoryId") String categoryId, @Param("brandId") String brandId, @Param("spuType") Integer spuType, @Param("status") Integer status, @Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime, @Param("pagination") Pagination pagination);

    /**
     * 按Id查询spuId
     *
     * @param spuId
     * @return
     */
    GoodsSpu getGoodsSpu(@Param("spuId") String spuId);

    /**
     * 删除指定spu的图片
     *
     * @param spuId
     * @return
     */
    Integer deleteSpuImg(@Param("spuId") String spuId);


    /**
     * 查询spu规格名列表
     *
     * @param spuId
     * @return
     */
    List<GoodsSpecName> listSpuSpec(@Param("spuId") String spuId);

    /**
     * 查询所有spu规格名
     *
     * @return
     */
    List<GoodsSpecName> allSpuSpec();

    /**
     * 删除spu的规格名
     *
     * @param spuId
     */
    Integer deleteSpuSpecName(@Param("spuId") String spuId);

    /**
     * 获取spu的收藏数
     *
     * @param spuId
     */
    Integer countCollection(@Param("spuId") String spuId);

    /**
     * 获取spu的购买数
     *
     * @param spuId
     */
    Integer countBuy(@Param("spuId") String spuId);

    /**
     * 获取用户的收藏列表
     *
     * @param userId
     * @param pagination
     * @return
     */
    List<GoodsCollection> listGoodsCollection(@Param("userId") String userId, @Param("pagination") Pagination pagination, @Param("spuType") Integer spuType);
}
