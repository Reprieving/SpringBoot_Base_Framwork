package com.balance.core.service;

import com.balance.core.dto.Pagination;
import com.balance.core.mybatis.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.List;

@Service
public class BaseService {

    @Autowired
    protected BaseMapper baseMapper;

    public void insert(Object entity) {
        baseMapper.insert(entity);
    }

    public void insertSelective(Object entity){
        baseMapper.insertSelective(entity);
    }

    public void delete(Object entity) {
        baseMapper.delete(entity);
    }

    public void update(Object entity){
        baseMapper.update(entity);
    }

    public <T> T selectById(Serializable id, Class<T> clazz) {
        return baseMapper.selectById(id, clazz);
    }

    public <T> List<T> selectListByIds(List<Object> idList, Class<T> clazz, Pagination pagination) throws Exception {
        return baseMapper.selectListByIds(idList,clazz,pagination);
    }

}
