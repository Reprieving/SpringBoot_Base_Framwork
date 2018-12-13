package com.balance.entity.shop;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;

@Data
@Alias("GoodsSpecNameValue")
@Table(name = "goods_spec_name_value")//规格名规格值关系表
public class GoodsSpecNameValue implements Serializable {
    private static final long serialVersionUID = -8183773527472145374L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Spec_name_id)
    private String specNameId;

    @Column(name = Id)
    private String specValueId;

    //DB Column name
    public static final String Id = "id";
    public static final String Spec_name_id = "spec_name_id";
    public static final String Spec_value_id = "spec_value_id";

    public GoodsSpecNameValue(){}

    public GoodsSpecNameValue(String specNameId, String specValueId) {
        this.specNameId = specNameId;
        this.specValueId = specNameId;
    }
}
