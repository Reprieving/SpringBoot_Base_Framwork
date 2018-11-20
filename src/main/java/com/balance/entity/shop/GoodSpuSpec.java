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

@Setter
@Getter
@Alias("GoodSpuSpec")
@Table(name = "goods_spu_spec")//spu规格表
public class GoodSpuSpec implements Serializable{
    private static final long serialVersionUID = -3614720667704245679L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "spu_id")
    private String spuId;//spu id（goods_spu表id）

    @Column(name = "spec_id")
    private String specId;//规格表id（goods_spec_name表id）

    @Column(name = "create_time")
    private Timestamp createTime;//创建时间

    @Column(name = "update_time")
    private Timestamp updateTime;//更新时间

}
