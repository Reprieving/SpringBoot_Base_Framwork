package com.balance.entity.user;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Alias("UserMerchantRecord")
@Table(name = "user_merchant_record")
public class UserMerchantRecord implements Serializable{
    private static final long serialVersionUID = -5541708179006779737L;

    @Id
    @Column(name = Id)
    public String id;

    @Column(name = User_id)
    public String userId;

    @Column(name = Merchant_name)
    public String merchantName;

    @Column(name = Merchant_rule_id)
    public String merchantRulerId;

    @Column(name = Create_time)
    public Timestamp createTime;

    @Column(name = Expire_time)
    public Timestamp expireTime;

    @Column(name = If_valid)
    public Boolean ifValid;


    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Merchant_name = "merchant_name";
    public static final String Merchant_rule_id = "merchant_ruler_id";
    public static final String Create_time = "create_time";
    public static final String Expire_time = "expire_time";
    public static final String If_valid = "if_valid";

    public UserMerchantRecord(String userId, String merchantRankName, String merchantRulerId, Timestamp createTimestamp, Timestamp expireTimestamp, Boolean ifValid) {
        this.userId = userId;
        this.merchantName = merchantRankName;
        this.merchantRulerId = merchantRulerId;
        this.createTime = createTimestamp;
        this.expireTime = expireTimestamp;
        this.ifValid = ifValid;
    }
}
