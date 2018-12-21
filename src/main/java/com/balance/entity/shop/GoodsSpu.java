package com.balance.entity.shop;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@Alias("GoodsSpu")
@Table(name = "goods_spu")//商品spu表
public class GoodsSpu implements Serializable{
    private static final long serialVersionUID = -4987682283871933822L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Spu_no)
    private String spuNo;//spu编号

    @Column(name = Goods_name)
    private String goodsName;//商品名称

    @Column(name = Goods_description)
    private String goodsDescription;//商品描述

    @Column(name = Package_unit)
    private String packageUnit;//包装单位

    @Column(name = Background_color)
    private String backgroundColor;//背景色

    @Column(name = Freight)
    private BigDecimal freight;//运费

    @Column(name = Low_price)
    private BigDecimal lowPrice;//最低价格

    @Column(name = Settlement_id)
    private Integer settlementId;//支付方式id

    @Column(name = Category_id)
    private String categoryId;//分类id（goods_category表id）

    @Column(name = Brand_id)
    private String brandId;//品牌id（goods_brand表id）

    @Column(name = Shop_id)
    private String shopId;//商铺id

    @Column(name = Default_img_url)
    private String defaultImgUrl;//spu 默认图片

    @Column(name = Create_time)
    private Timestamp createTime;

    @Column(name = Update_time)
    private Timestamp updateTime;

    //ShopConst.SPU_TYPE_*
    @Column(name = Spu_type)
    private Integer spuType;//spu类型

    //ShopConst.GOODS_STATUS_*
    @Column(name = Status)
    private Integer status;//sku状态

    @Column(name = If_valid)
    private Boolean ifValid;//是否有效

    //扩展属性
    //类目名
    private String categoryName;
    //品牌名
    private String brandName;
    //分页对象
    private Pagination pagination;
    //分页页码
    private Integer pageNum;
    //分页条目数
    private Integer pageSize;
    //所有图片集合
    private List<GoodsImg> allImg;
    //默认图对象
    private  List<GoodsImg> defaultImg;
    //详情图url
    private List<String> detailImgUrl;
    //详情图对象列表
    private List<GoodsImg> detailImg;
    //介绍图url
    private List<String> introduceImgUrl;
    //介绍图对象列表
    private List<GoodsImg> introduceImg;
    //条件查询开始时间
    private Timestamp startTime;
    //条件查询结束时间
    private Timestamp endTime;
    //规格名Id
    private List<String> specNameIdList;



    //DB Column name
    public static final String Id = "id";
    public static final String Spu_no = "spu_no";
    public static final String Package_unit = "package_unit";
    public static final String Background_color = "background_color";
    public static final String Goods_name = "goods_name";
    public static final String Goods_description = "goods_description";
    public static final String Freight = "freight";
    public static final String Low_price = "low_price";
    public static final String Settlement_id = "settlement_id";
    public static final String Category_id = "category_id";
    public static final String Brand_id = "brand_id";
    public static final String Shop_id = "shop_id";
    public static final String Default_img_url = "default_img_url";
    public static final String Create_time = "create_time";
    public static final String Update_time = "update_time";
    public static final String Spu_type = "spu_type";
    public static final String Status = "status";
    public static final String If_valid = "if_valid";

    public Pagination buildPagination() {
        Pagination pagination =  new Pagination();
        pagination.setPageNum(this.pageNum);
        pagination.setPageSize(this.pageSize);
        return pagination;
    }
}
