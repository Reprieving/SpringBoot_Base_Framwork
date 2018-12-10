package com.balance.mapper.information;

import com.balance.entity.information.Article;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {

    List<Article> selectByPage(Map<String, Object> param);

    int selectCount(Map<String, Object> param);
}
