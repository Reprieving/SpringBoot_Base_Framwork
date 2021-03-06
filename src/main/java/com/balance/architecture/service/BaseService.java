package com.balance.architecture.service;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.mybatis.mapper.BaseMapper;
import com.balance.architecture.utils.ValueCheckUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class BaseService {
    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    protected BaseMapper baseMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public <T> Integer insert(T entity) {
        return baseMapper.insert(entity.getClass(), entity);
    }

    public <T> Integer insertIfNotNull(T entity) {
        return baseMapper.insertIfNotNull(entity.getClass(), entity);
    }

    public <T> Integer insertBatch(List<T> entityList,Boolean insertNull) {
        Integer flag;
        ValueCheckUtils.notEmpty(entityList, "entityList can't be null");

        Class clazz = entityList.get(0).getClass();
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        int size = 100;
        int j = 0;
        try {
            for (int i = 0; i < entityList.size(); i++) {
                if(insertNull){
                    baseMapper.insert(clazz, entityList.get(i));
                }else {
                    baseMapper.insertIfNotNull(clazz, entityList.get(i));
                }
                j++;
                if (j % size == 0 || i == entityList.size() - 1) {
                    session.commit();
                    session.clearCache();
                }
            }
            flag = 1;
        } catch (Exception e) {
            flag = 0;
            session.rollback();
        } finally {
            session.close();
        }
        return flag;
    }

    public Integer delete(Object entity) {
        return baseMapper.delete(entity.getClass(), entity);
    }

    public Integer update(Object entity) {
        return baseMapper.update(entity.getClass(), entity);
    }

    public <T> T selectOneById(Serializable id, Class<T> clazz) {
        try {
            return baseMapper.selectById(id, clazz);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public <T> T selectOneByWhereString(String whereStr,Object whereValue, Class<T> clazz) {
        try {
            Map<String,Object> whereMap = ImmutableMap.of(whereStr,whereValue);
            return baseMapper.selectOneByWhereMap(whereMap, clazz);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public <T> T selectOneByWhereMap(Map<String,Object> paramMap, Class<T> clazz) {
        try {
            return baseMapper.selectOneByWhereMap(paramMap, clazz);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public <T> List<T> selectAll(Class<T> clazz, Pagination pagination) {
        try {
            return (List<T>) baseMapper.selectAll(clazz, pagination).get(0);
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    public <T> List<T> selectListByWhereString(String whereStr, Object whereValue, Class<T> clazz, Pagination pagination) {
        try {
            Map<String,Object> whereMap = ImmutableMap.of(whereStr,whereValue);
            T o = baseMapper.selectListByWhere(whereMap,clazz, pagination).get(0);
            if(!(o instanceof List)){
                return Arrays.asList(o);
            }
            return (List<T>) o;
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

}
