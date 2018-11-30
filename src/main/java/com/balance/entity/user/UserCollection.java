package com.balance.entity.user;


import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Alias("UserCollection")
@Table(name = "user_collection")
public class UserCollection implements Serializable {
    private static final long serialVersionUID = -3481512245256625366L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = User_id)
    private String userId;

    @Column(name = Article_id)
    private String articleId;

    @Column(name = Article_title)
    private String articleTitle;

    @Column(name = Article_type)
    private Integer articleType;

    @Column(name = Create_time)
    private Timestamp createTime;

    //DB Column name
    public static final String Id = "id";
    public static final String User_id = "user_id";
    public static final String Article_id = "article_id";
    public static final String Article_title = "article_title";
    public static final String Article_type = "article_type";
    public static final String Create_time = "create_time";

    public UserCollection(String userId, String articleId, String articleTitle, Integer articleType) {
        this.userId = userId;
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleType = articleType;
    }
}
