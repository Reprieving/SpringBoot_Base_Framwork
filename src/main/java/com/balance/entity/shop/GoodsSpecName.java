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
    @Column(name = "id")
    private String id;

    @Column(name = "spec_no")
    private String specNo;//规格编号

    @Column(name = "spec_name")
    private String specName;//规格名字

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;
}
