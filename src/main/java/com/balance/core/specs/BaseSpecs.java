package com.balance.core.specs;

import com.balance.core.dto.Pagination;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface BaseSpecs {
    void insert(Object entity);
    void insertSelective(Object entity);
    void delete(Object entity);
    void update(Object entity);
    <T> T selectById(T entity);
    <T> List<T> selectListByIds(Collection<? extends Serializable> idList,
                            Class<T> clazz,
                            Pagination pagination);
}
