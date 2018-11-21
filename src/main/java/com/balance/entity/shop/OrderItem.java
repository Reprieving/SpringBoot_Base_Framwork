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

    @Column(name = "goods_spu_id")
    private String goodsSpuId;//spu id

    @Column(name = "goods_sku_id")
    private String goodsSKuId;//sku id

    @Column(name = "number")
    private Integer number;//数量

    @Column(name = "price")
    private BigDecimal price;//单价

    @Column(name = "total_price")
    private BigDecimal total_price;//总价

}
