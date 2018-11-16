package com.balance.architecture.mybatis.mapper;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.mybatis.provider.MysqlProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface BaseMapper {
    /**
     * insert one record
     * @param clazz
     * @param entity
     */
    @InsertProvider(type = MysqlProvider.class, method = "insert")
    Integer insert(@Param(MysqlProvider.CLAZZ) Object clazz, @Param(MysqlProvider.ENTITY) Object entity);

    /**
     * insert one record's not null property
     * @param clazz
     * @param entity
     */
    @InsertProvider(type = MysqlProvider.class, method = "insertIfNotNull")
    Integer insertIfNotNull(@Param(MysqlProvider.CLAZZ) Object clazz, @Param(MysqlProvider.ENTITY) Object entity);

    /**
     * delete one record
     * @param clazz
     * @param entity
     */
    @DeleteProvider(type = MysqlProvider.class, method = "delete")
    Integer delete(@Param(MysqlProvider.CLAZZ) Object clazz, @Param(MysqlProvider.ENTITY) Object entity);

    /**
     * update one record
     * @param clazz
     * @param entity
     */
    @UpdateProvider(type = MysqlProvider.class, method = "update")
    Integer update(@Param(MysqlProvider.CLAZZ) Object clazz, @Param(MysqlProvider.ENTITY) Object entity);

    /**
     * select one by id
     * @param id
     * @param tClass
     * @param <T>
     * @return
     */
    @SelectProvider(type = MysqlProvider.class, method = "selectById")
    <T> T selectById(@Param(MysqlProvider.ID_VALUE) Serializable id, @Param(MysqlProvider.CLAZZ) Class<T> tClass);

    /**
     * select one by where string
     * @param whereStr
     * @param value
     * @param tClass
     * @param <T>
     * @return
     */
    @SelectProvider(type = MysqlProvider.class, method = "selectOneByWhere")
    <T> T selectOneByWhere(@Param(MysqlProvider.WHERE_STR) String whereStr, @Param(MysqlProvider.WHERE_VALUE) Object value, @Param(MysqlProvider.CLAZZ) Class<T> tClass);

    /**
     * select one by where map
     * @param paramMap
     * @param tClass
     * @param <T>
     * @return
     */
    @SelectProvider(type = MysqlProvider.class, method = "selectOneByWhereMap")
    <T> T selectOneByWhereMap(@Param(MysqlProvider.PARAM_MAP) Map<String, Object> paramMap, @Param(MysqlProvider.CLAZZ) Class<T> tClass);

    /**
     * select all
     * @param clazz
     * @param pagination
     * @param <T>
     * @return
     */
    @SelectProvider(type = MysqlProvider.class, method = "selectAll")
    <T> List<T> selectAll(@Param(MysqlProvider.CLAZZ) Class<T> clazz, @Param(MysqlProvider.PAGINATION) Pagination pagination);

    /**
     * select list by where string
     * @param whereStr
     * @param value
     * @param clazz
     * @param pagination
     * @param <T>
     * @return
     */
    @SelectProvider(type = MysqlProvider.class, method = "selectListByWhere")
    <T> List<T> selectListByWhere(@Param(MysqlProvider.WHERE_STR) String whereStr, @Param(MysqlProvider.WHERE_VALUE) Object value, @Param(MysqlProvider.CLAZZ) Class<T> clazz, @Param(MysqlProvider.PAGINATION) Pagination pagination);

    /**
     * select list by where map
     * @param paramMap
     * @param clazz
     * @param pagination
     * @param <T>
     * @return
     */
    @SelectProvider(type = MysqlProvider.class, method = "selectListByWhereMap")
    <T> List<T> selectListByWhereMap(@Param(MysqlProvider.PARAM_MAP) Map<String, Object> paramMap, @Param(MysqlProvider.CLAZZ) Class<T> clazz, @Param(MysqlProvider.PAGINATION) Pagination pagination);


}
