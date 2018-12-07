package com.balance.mapper.information;

import com.balance.entity.information.Article;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {

    List<Article> selectByPage4Admin(Map<String, Object> param);

    int selectCount4Admin(Map<String, Object> param);

    List<Article> selectByPage4User(Map<String, Object> param);

    int selectCount4User(Map<String, Object> param);
}
