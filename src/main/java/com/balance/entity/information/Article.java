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
    private String articleTitle; //标题

    @Column(name = Article_cover)
    private String articleCover; //封面

    @Column(name = Article_content)
    private String articleContent; //内容

    @Column(name = Article_type)
    private Integer articleType;//文章类型 InformationConst

    @Column(name = Content_type)
    private Integer contentType; //内容类型 1. 空 2: 链接, 3:富文本, 4:商品

    @Column(name = IfValid)
    private Boolean ifValid; //有效性

    @Column(name = Sort)
    private Integer sort; //排序 大的排前面

    @Column(name = VerifyStatus)
    private Integer verifyStatus; //审核状态

    @Column(name = CreateBy)
    private String createBy; //发布人

    @Column(name = CreateTime)
    private Timestamp createTime; //发布时间

    //扩展属性
    private Integer ifLike; //是否点赞
    private Integer likeAmount; //点赞数
    private Integer ifCollect; //是否收藏
    private String goodsName; //商品名称

    //DB Column name
    public static final String Id = "id";
    public static final String Article_title = "article_title";
    public static final String Article_cover = "article_cover";
    public static final String Article_content = "article_content";
    public static final String Article_type = "article_type";
    public static final String Content_type = "content_type";
    public static final String IfValid = "if_valid";
    public static final String Sort = "sort";
    public static final String VerifyStatus = "verify_status";
    public static final String CreateBy = "create_by";
    public static final String CreateTime = "create_time";


}
