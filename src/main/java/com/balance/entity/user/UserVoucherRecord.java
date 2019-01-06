package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;
import sun.misc.Version;

import java.io.Serializable;
import java.sql.Timestamp;

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

    @Column(name = Quantity)
    public Integer quantity;

    @Column(name = If_valid)
    public Boolean ifValid;

    @Column(name = Enable_day_time)
    public Integer enableDayTime; //

    @Column(name = Create_time)
    public Timestamp createTime;

    @Column(name = Unable_time)
    public Timestamp unableTime;

    @Column(name = Status)
    public Integer status;


    //扩展属性
    public String voucherName;
    public Integer voucherType;
    public String shopId;
    public String shopName;
    public String description;

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Voucher_id = "voucher_id";
    public static final String Quantity = "quantity";
    public static final String If_valid = "if_valid";
    public static final String Enable_day_time = "enable_day_time";
    public static final String Create_time = "create_time";
    public static final String Unable_time = "unable_time";
    public static final String Description = "description";
    public static final String Status = "status";
    public static final String Version = "version";

    public UserVoucherRecord(){}

    public UserVoucherRecord(String userId, Integer quantity,String voucherId,Timestamp unableTime) {
        this.userId = userId;
        this.quantity = quantity;
        this.voucherId = voucherId;
        this.unableTime = unableTime;
    }

    public static void main(String[] args) {
        UserVoucherRecord record = new UserVoucherRecord();
        System.out.println(record.hashCode());

        UserVoucherRecord record1 = new UserVoucherRecord();
        System.out.println(record1.hashCode());
    }

}
