package com.balance.entity.shop;


import com.balance.architecture.dto.Pagination;
import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.bouncycastle.util.Times;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@Alias("OrderInfo")
@Table(name = "order_info")//订单基本信息表
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 5290596013449535746L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Order_no)
    private String orderNo; //订单编号

    @Column(name = Settlement_id)
    private Integer settlementId; //支付方式

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = Shop_id)
    private String shopId;//店铺id

    @Column(name = User_name)
    private String userName; //用户昵称

    @Column(name = Address_id)
    private String addressId;//地址id

    @Column(name = Freight)
    private BigDecimal freight;//运费

    @Column(name = Price)
    private BigDecimal price; //订单金额

    @Column(name = Status)
    private Integer status;//订单状态

    @Column(name = If_investigation)
    private Boolean ifInvestigation; //是否已提交问卷调查

    @Column(name = If_pay)
    private Boolean ifPay;//是否已支付

    @Column(name = If_valid)
    private Boolean ifValid;//是否有效

    @Column(name = Logistic_number)
    private String logisticNumber; //物流编码

    @Column(name = Create_time)
    private Timestamp createTime;//创建时间

    @Column(name = Pay_time)
    private Timestamp payTime;//支付时间

    @Column(name = Order_type)
    private Integer orderType;//订单类型


    //扩展属性
    private Timestamp startTime;
    private Timestamp endTime;
    private Pagination pagination;

    public OrderInfo() {}

    public OrderInfo(
            String orderNo, Integer settlementId, String userId, String shopId, String userName, String addressId, BigDecimal orderTotalPrice,BigDecimal orderTotalFreight,
            Integer orderType,Timestamp createTime
    ) {
        this.orderNo = orderNo;
        this.settlementId = settlementId;
        this.userId = userId;
        this.shopId = shopId;
        this.userName = userName;
        this.addressId = addressId;
        this.price = orderTotalPrice;
        this.freight = orderTotalFreight;
        this.orderType = orderType;
        this.createTime = createTime;
    }

    //DB Column name
    public static final String Id = "id";
    public static final String Order_no = "order_no";
    public static final String Settlement_id = "settlement_id";
    public static final String User_id = "user_id";
    public static final String Shop_id = "shop_id";
    public static final String Shop_name = "shop_name";
    public static final String User_name = "user_name";
    public static final String Address_id = "address_id";
    public static final String Price = "price";
    public static final String Freight = "freight";
    public static final String Status = "status";
    public static final String If_investigation = "if_investigation";
    public static final String If_pay = "if_pay";
    public static final String Logistic_number = "logistic_number";
    public static final String Create_time = "create_time";
    public static final String If_valid = "if_valid";
    public static final String Pay_time = "pay_time";
    public static final String Order_type = "order_type";
}
