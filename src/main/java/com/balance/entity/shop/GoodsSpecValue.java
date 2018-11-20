package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@Alias("GoodsSpecValue")
@Table(name = "goods_spec_value")//规格值表
public class GoodsSpecValue implements Serializable {

    private static final long serialVersionUID = 6675842731486652762L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "spec_id")
    private String specId; //规格表id（goods_spec_name表id）

    @Column(name = "spec_value")
    private String specValue;//规格值

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

}
