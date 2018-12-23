package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Date;

@Data
@Alias("BankInfo")
@Table(name = "bank_info")
public class BankInfo  implements Serializable {
    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Name)
    private String name;

    @Column(name = Icon)
    private String icon;

    @Column(name = Create_time)
    private Date createTime;


    //DB Column name
    public static final String Id = "id";
    public static final String Name = "name";
    public static final String Icon = "icon";
    public static final String Create_time = "create_time";
}