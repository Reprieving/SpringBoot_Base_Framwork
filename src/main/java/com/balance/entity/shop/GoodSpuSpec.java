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
@Alias("GoodSpuSpec")
@Table(name = "goods_spu_spec")//spu规格表
public class GoodSpuSpec implements Serializable{
    private static final long serialVersionUID = -3614720667704245679L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Spu_id)
    private String spuId;//spu id（goods_spu表id）

    @Column(name = Spec_id)
    private String specId;//规格表id（goods_spec_name表id）

    @Column(name = Create_time)
    private Timestamp createTime;//创建时间

    @Column(name = Update_time)
    private Timestamp updateTime;//更新时间

    //DB Column name
    public static final String Id = "id";
    public static final String Spu_id = "spu_id";
    public static final String Spec_id = "spec_id";
    public static final String Create_time = "create_time";
    public static final String Update_time = "update_time";
}
