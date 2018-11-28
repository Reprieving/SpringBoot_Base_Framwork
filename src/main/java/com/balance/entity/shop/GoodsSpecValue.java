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
@Alias("GoodsSpecValue")
@Table(name = "goods_spec_value")//规格值表
public class GoodsSpecValue implements Serializable {

    private static final long serialVersionUID = 6675842731486652762L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Spec_id)
    private String specId; //规格表id（goods_spec_name表id）

    @Column(name = Spec_value)
    private String specValue;//规格值

    @Column(name = Create_time)
    private Timestamp createTime;

    @Column(name = Update_time)
    private Timestamp updateTime;

    //DB Column name
    public static final String Id = "id";
    public static final String Spec_id = "spec_id";
    public static final String Spec_value = "spec_value";
    public static final String Create_time = "create_time";
    public static final String Update_time = "update_time";

}
