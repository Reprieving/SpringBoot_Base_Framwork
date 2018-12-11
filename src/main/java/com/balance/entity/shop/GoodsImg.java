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
    @Column(name = Id)
    private String id;

    @Column(name = Spu_id)
    private String spuId;//spu id（goods_spu表id）

    @Column(name = Sku_id)
    private String skuId;//spu id（goods_sku表id）

    @Column(name = Img_url)
    private String imgUrl;//商品图片url

    //ShopConst.GOODS_IMG_TYPE_*
    @Column(name = Img_type)
    private Integer imgType;//商品图片类型

    @Column(name = Create_time)
    private String createTime;//创建时间

    //扩展属性
    private String name;
    private String url;

    public GoodsImg(){}

    public GoodsImg(String spuId, String imgUrl, int imgType) {
        this.spuId = spuId;
        this.imgUrl = imgUrl;
        this.imgType = imgType;
    }

    //DB Column name
    public static final String Id = "id";
    public static final String Spu_id = "spu_id";
    public static final String Sku_id = "sku_id";
    public static final String Img_url = "img_url";
    public static final String Img_type = "img_type";
    public static final String Create_time = "create_time";
}
