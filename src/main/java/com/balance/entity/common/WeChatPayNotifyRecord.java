package com.balance.entity.common;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

//微信支付通知记录
@Data
@Alias("WeChatPayNotifyRecord")
@Table(name = "wechatpay_notify_record")
public class WeChatPayNotifyRecord implements Serializable{
    private static final long serialVersionUID = -4634723361470808220L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Mch_id)
    private String mchId; //微信商户id

    @Column(name = Mch_order_id)
    private String mchOrderId;//商户系统订单id

    @Column(name = Wx_order_id)
    private String wxOrderId; //微信支付订单id

    @Column(name = Wx_app_id)
    private String wxAppId; //微信平台APP id

    @Column(name = Wx_open_id)
    private String openId; //微信open id

    @Column(name = Order_price)
    private BigDecimal orderPrice; //订单金额

    @Column(name = Create_time)
    private Timestamp createTime; //创建时间


    //DB Column name
    public static final String Id = "id";
    public static final String Mch_id = "mch_id";
    public static final String Mch_order_id = "mch_order_id";
    public static final String Wx_order_id = "wx_order_id";
    public static final String Wx_app_id = "wx_app_id";
    public static final String Wx_open_id = "wx_open_id";
    public static final String Order_price = "order_price";
    public static final String Create_time = "create_time";

    public WeChatPayNotifyRecord(String mchId, String mchOrderId, String wxOrderId, String wxAppId, String openId, BigDecimal orderPrice, Timestamp createTime) {
        this.mchId = mchId;
        this.mchOrderId = mchOrderId;
        this.wxOrderId = wxOrderId;
        this.wxAppId = wxAppId;
        this.openId = openId;
        this.orderPrice = orderPrice;
        this.createTime = createTime;
    }
}
