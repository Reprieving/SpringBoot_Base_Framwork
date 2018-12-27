package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("UserMemberRecord")
@Table(name = "user_member_record")
public class UserMemberRecord implements Serializable{
    private static final long serialVersionUID = -8051628499759637028L;

    @Id
    @Column(name = Id)
    public String id;

    @Column(name = User_id)
    public String userId;

    @Column(name = Pay_amount)
    public BigDecimal payAmount;

    @Column(name = Member_type)
    public Integer memberType;

    @Column(name = Expire_time)
    public Integer expireTime;

    @Column(name = If_valid)
    public Boolean ifValid;

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Pay_amount = "pay_amount";
    public static final String Member_type = "member_type";
    public static final String Expire_time = "expire_time";
    public static final String If_valid = "if_valid";
}
