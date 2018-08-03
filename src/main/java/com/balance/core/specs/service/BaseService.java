package com.balance.core.specs.service;

import com.balance.core.dto.Pagination;
import com.balance.core.mybatis.mapper.BaseMapper;
import com.balance.core.specs.BaseSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Service
public class BaseService implements BaseSpecs{

    @Autowired
    protected BaseMapper baseMapper;

    public void insert(Object entity) {
        baseMapper.insert(entity);
    }

    public void insertSelective(Object entity) {
        baseMapper.insertSelective(entity);
    }

    public void delete(Object entity) {
        baseMapper.delete(entity);
    }

    public void update(Object entity) {
        baseMapper.update(entity);
    }

    public <T> T selectById(T t) {
        return baseMapper.selectById(t);
    }

    public <T> List<T> selectListByIds(Collection<? extends Serializable> idList, Class<T> clazz, Pagination pagination) {
        return baseMapper.selectListByIds(idList,clazz,pagination);
    }

}
