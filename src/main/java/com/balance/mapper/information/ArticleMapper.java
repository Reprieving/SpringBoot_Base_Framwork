package com.balance.mapper.information;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.information.Article;

import java.util.List;
import java.util.Map;

public interface ArticleMapper {

    List<Article> selectByPage(Pagination<Article> pagination);

}
