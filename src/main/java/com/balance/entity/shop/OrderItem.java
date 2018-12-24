package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.core.annotation.Order;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("OrderItem")
@Table(name = "order_item")//订单详情表
public class OrderItem implements Serializable{
    private static final long serialVersionUID = 7195245867662678593L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Order_id)
    private String orderId;//order id

    @Column(name = Goods_spu_id)
    private String goodsSpuId;//spu id

    @Column(name = Goods_sku_id)
    private String goodsSkuId;//sku id

    @Column(name = Number)
    private Integer number;//数量

    @Column(name = Price)
    private BigDecimal price;//spu单价

    @Column(name = Total_price)
    private BigDecimal totalPrice;//spu总价

    @Column(name = Status)
    private Integer status;


    public OrderItem(){}

    public OrderItem(String goodsSpuId, String goodsSkuId, Integer number, BigDecimal price, BigDecimal totalPrice) {
        this.goodsSpuId = goodsSpuId;
        this.goodsSkuId = goodsSkuId;
        this.number = number;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    //DB Column name
    public static final String Id = "id";
    public static final String Order_id = "order_id";
    public static final String Goods_spu_id = "goods_spu_id";
    public static final String Goods_sku_id = "goods_sku_id";
    public static final String Number = "number";
    public static final String Price = "price";
    public static final String Total_price = "total_price";
    public static final String Status = "status";

    //----扩展属性
    //spu id
    private String spuId;

    //spu名称
    private String spuName;

    //spu描述
    private String spuDescription;

    //spu默认图片
    private String imgUrl;

    //规格名规格值id json 格式：{"规格名id":"规格值id"}
    private String specJson;

    //规格名规格值 json 格式："规格名:规格值 规格名:规格值 规格名:规格值"
    private String specStr;

    private Integer settlementId;

}
