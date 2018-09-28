package com.balance.core.mybatis.mapper;

import com.balance.core.dto.Pagination;
import com.balance.core.mybatis.MapKeyConst;
import com.balance.core.mybatis.provider.MysqlProvider;
import com.balance.work.entity.AppInterface;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;

@Repository
public interface BaseMapper {
    @InsertProvider(type = MysqlProvider.class,method = "insert")
    void insert(@Param(MysqlProvider.CLAZZ)Object entity) throws Exception;

    @InsertProvider(type = MysqlProvider.class,method = "insertSelective")
    void insertSelective(@Param(MysqlProvider.CLAZZ)Object entity) throws Exception;

    @DeleteProvider(type = MysqlProvider.class,method = "delete")
    void delete(@Param(MysqlProvider.CLAZZ)Object entity) throws Exception;

    @UpdateProvider(type = MysqlProvider.class,method = "update")
    void update(@Param(MysqlProvider.CLAZZ)Object entity) throws Exception;

    @SelectProvider(type = MysqlProvider.class,method = "selectById")
    <T> T selectById(@Param(MysqlProvider.ID_VALUE) Serializable id,@Param(MysqlProvider.CLAZZ) Class<T> tClass) throws Exception;

    @SelectProvider(type = MysqlProvider.class,method = "selectListByIds")
    <T> List<T> selectListByIds(@Param(MysqlProvider.PARAM_LIST) List<Object> idList,
                            @Param(MysqlProvider.CLAZZ) Class<T> clazz,
                            @Param(MysqlProvider.PAGINATION) Pagination pagination) throws Exception;

}
