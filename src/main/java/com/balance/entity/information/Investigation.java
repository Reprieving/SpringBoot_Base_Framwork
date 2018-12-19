package com.balance.entity.information;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Alias("Investigation")
@Table(name = "information_investigation")
public class Investigation implements Serializable{
    private static final long serialVersionUID = 4635545283271403293L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId; //用户id

    @Column(name = Beauty_id)
    private String beautyId; //美妆品id

    @Column(name = Order_id)
    private String orderId; //美妆品id

    @Column(name = Investigation_title)
    private String investigationTitle; //标题

    @Column(name = Investigation_content)
    private String investigationContent; //内容

    @Column(name = Template_id)
    private String templateId; //模板ID, 0为模板

    @Column(name = Depict)
    private String depict; //问卷描述

    @Column(name = CreateTime)
    private Timestamp createTime; //提交时间

    // 扩展
    /** 商品名称  */
    private String goodsName;

    /** 用户昵称  */
    private String userName;

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Beauty_id = "beauty_id";
    public static final String Order_id = "order_id";
    public static final String Investigation_title = "investigation_title";
    public static final String Depict = "depict";
    public static final String Investigation_content = "investigation_content";
    public static final String Template_id = "template_id";
    public static final String CreateTime = "create_time";

}
