package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("GoodsImg")
@Table(name = "goods_img")//商品图片表
public class GoodsImg implements Serializable {

    private static final long serialVersionUID = 5215064699572767869L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "spu_id")
    private String spuId;//spu id（goods_spu表id）

    @Column(name = "sku_id")
    private String skuId;//spu id（goods_sku表id）

    @Column(name = "img_url")
    private String imgUrl;//商品图片url

    //ShopConst.GOODS_IMG_TYPE_*
    @Column(name = "img_type")
    private String imgType;//商品图片类型

    @Column(name = "create_time")
    private String createTime;//创建时间

}
