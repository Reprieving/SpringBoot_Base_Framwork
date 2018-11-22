package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("OrderItem")
@Table(name = "order_item")//订单详情表
public class OrderItem implements Serializable{
    private static final long serialVersionUID = 7195245867662678593L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "order_id")
    private String orderId;//order id

    @Column(name = "goods_spu_id")
    private String goodsSpuId;//spu id

    @Column(name = "goods_sku_id")
    private String goodsSkuId;//sku id

    @Column(name = "number")
    private Integer number;//数量

    @Column(name = "price")
    private BigDecimal price;//单价

    @Column(name = "total_price")
    private BigDecimal totalPrice;//总价

    public OrderItem(String goodsSpuId, String goodsSkuId, Integer number, BigDecimal price, BigDecimal totalPrice) {
        this.goodsSpuId = goodsSpuId;
        this.goodsSkuId = goodsSkuId;
        this.number = number;
        this.price = price;
        this.totalPrice = totalPrice;
    }



    //----扩展属性
    //spu id
    private String spuId;

    //spu name
    private String spuName;

    //spu默认图片
    private String imgUrl;

    //规格名规格值id json 格式：{"规格名id":"规格值id"}
    private String specJson;

    //规格名规格值 json 格式："规格名:规格值 规格名:规格值 规格名:规格值"
    private String specStr;
}
