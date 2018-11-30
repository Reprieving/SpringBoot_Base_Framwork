package com.balance.entity.information;

import com.balance.architecture.mybatis.annotation.Column;
import com.balance.architecture.mybatis.annotation.Id;
import com.balance.architecture.mybatis.annotation.Table;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Alias("Article")
@Table(name = "information_article")
public class Article implements Serializable{
    private static final long serialVersionUID = -5742334582756809679L;

    @Id
    @Column(name = Id)
    private String id;

    @Column(name = Article_title)
    private String article_title; //标题

    @Column(name = Article_content)
    private String articleContent; //内容

    @Column(name = Article_type)
    private Integer articleType;//文章类型InformationConst.ARTICLE_TYPE_*

    @Column(name = IsValid)
    private Boolean isValid; //有效性

    @Column(name = VerifyStatus)
    private Integer verifyStatus; //审核状态

    @Column(name = CreateBy)
    private String createBy; //发布人

    @Column(name = CreateTime)
    private Timestamp createTime; //发布时间


    //DB Column name
    public static final String Id = "id";
    public static final String Article_title = "article_title";
    public static final String Article_content = "article_content";
    public static final String Article_type = "article_type";
    public static final String IsValid = "is_valid";
    public static final String VerifyStatus = "verify_status";
    public static final String CreateBy = "create_by";
    public static final String CreateTime = "create_time";
}
