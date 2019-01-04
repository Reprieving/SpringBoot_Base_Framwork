package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 商家(节点)用户申请
 */
@Data
@Alias("UserMerchantApply")
@Table(name = "user_merchant_apply")
public class UserMerchantApply implements Serializable {

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId;//用户ID

    @Column(name = Merchant_ruler_id)
    private String merchantRulerId;//商户配置表ID

    @Column(name = Full_name)
    private String fullName;//姓名

    @Column(name = Location)
    private String location;//地区

    @Column(name = Telephone)
    private String telephone;//联系电话

    @Column(name = Email)
    private String email;//邮箱

    @Column(name = State)
    private Integer state;//状态 0.待联系, 1. 已联系, 2. 通过, 3. 不通过

    @Column(name = Create_time)
    private Timestamp createTime;


    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Merchant_ruler_id = "merchant_ruler_id";
    public static final String Location = "location";
    public static final String Full_name = "full_name";
    public static final String Telephone = "telephone";
    public static final String Email = "email";
    public static final String State = "state";
    public static final String Create_time = "create_time";

}
