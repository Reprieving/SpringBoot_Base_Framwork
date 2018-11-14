package com.balance.architecture.service;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.mybatis.mapper.BaseMapper;
import com.balance.client.NettyClient;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class BaseService {
    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    protected BaseMapper baseMapper;


    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Transactional
    public <T> void insert(T entity) {
        baseMapper.insert(entity.getClass(), entity);
    }

    @Transactional
    public <T> void insertIfNotNull(T entity) {
        baseMapper.insertIfNotNull(entity.getClass(), entity);

    }

    @Transactional
    public <T> void insertBatch(List<T> entityList) {
        Class clazz = entityList.get(0).getClass();
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        int size = 100;
        int j = 0;
        try {
            for (int i = 0; i < entityList.size(); i++) {
                baseMapper.insert(clazz, entityList.get(i));
                j++;
                if (j % size == 0 || i == entityList.size() - 1) {
                    session.commit();
                    session.clearCache();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        } finally {
            session.close();
        }

    }

    @Transactional
    public void delete(Object entity) {
        baseMapper.delete(entity.getClass(), entity);
    }

    @Transactional
    public void update(Object entity) {
        baseMapper.update(entity.getClass(), entity);
    }

    public <T> T selectById(Serializable id, Class<T> clazz) {
        try {
            return baseMapper.selectById(id, clazz);
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
}
