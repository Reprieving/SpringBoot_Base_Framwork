package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("UserVoucherRecord")
@Table(name = "user_voucher_record")
public class UserVoucherRecord implements Serializable{
    private static final long serialVersionUID = -5078713370916794501L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    public String userId;

    @Column(name = Voucher_id)
    public String voucherId;

    @Column(name = If_valid)
    public Boolean ifValid;

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Voucher_id = "voucher_id";
    public static final String If_valid = "if_valid";

    public UserVoucherRecord(){}

    public UserVoucherRecord(String userId, String voucherId) {
        this.userId = userId;
        this.voucherId = voucherId;
    }
}
