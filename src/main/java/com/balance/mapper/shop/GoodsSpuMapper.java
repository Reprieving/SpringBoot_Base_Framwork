package com.balance.mapper.shop;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.shop.GoodsSpu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsSpuMapper {
    List<GoodsSpu> listGoodsSpu(@Param("goodsName") String name, @Param("categoryId") String categoryId, @Param("brandId") String brandId,@Param("spuType") Integer spuType, @Param("status")Integer status, @Param("pagination") Pagination pagination);
}
