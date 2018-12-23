package com.balance.entity.user;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by weihuaguo on 2018/12/23 18:41.
 */
@Data
@Alias("Feedback")
@Table(name = "feed_back")
public class Feedback implements Serializable {
    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Type)
    private Integer type;

    @Column(name = User_id)
    private String userId;

    @Column(name = Content)
    private String content;

    @Column(name = Create_time)
    private Timestamp createTime;


    //DB Column name
    public static final String Id = "id";
    public static final String Type = "type";
    public static final String User_id = "user_id";
    public static final String Content = "content";
    public static final String Create_time = "create_time";

}
