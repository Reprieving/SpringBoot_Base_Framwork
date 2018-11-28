package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Alias("GoodsSpecName")
@Table(name = "goods_spec_name")//规格名称表
public class GoodsSpecName implements Serializable{

    private static final long serialVersionUID = -4658507825537631178L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Spec_no)
    private String specNo;//规格编号

    @Column(name = Spec_name)
    private String specName;//规格名字

    @Column(name = Create_time)
    private Timestamp createTime;

    @Column(name = Update_time)
    private Timestamp updateTime;

    //DB Column name
    public static final String Id = "id";
    public static final String Spec_no = "spec_no";
    public static final String Spec_name = "spec_name";
    public static final String Create_time = "create_time";
    public static final String Update_time = "update_time";
}
