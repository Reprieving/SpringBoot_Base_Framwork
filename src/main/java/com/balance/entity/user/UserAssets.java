package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Alias("UserAssets")
@Table(name = "user_assets")
public class UserAssets implements Serializable{ //用户资产
    private static final long serialVersionUID = 5279191356481637406L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = Compute_power)
    private BigDecimal computePower; //算力

    @Column(name = Ih)
    private BigDecimal ih; //IH(美钻)

    @Column(name = Eth)
    private BigDecimal eth; //ETH

    @Column(name = Ore)
    private BigDecimal ore; //矿石

    @Column(name = Version)
    private Integer version; //版本

    //扩展
    private String userName;//用户名
    private Long rankNo;//颜值排名

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Compute_power = "compute_power";
    public static final String Ih = "ih";
    public static final String Eth = "eth";
    public static final String Ore = "ore";
    public static final String Version = "version";

    public UserAssets(){

    }
}
