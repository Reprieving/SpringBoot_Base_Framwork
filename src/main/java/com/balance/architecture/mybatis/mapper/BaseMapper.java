package com.balance.architecture.mybatis.mapper;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.mybatis.provider.MysqlProvider;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface BaseMapper {
    @InsertProvider(type = MysqlProvider.class,method = "insert")
    void insert(@Param(MysqlProvider.CLAZZ)Object entity);

    @InsertProvider(type = MysqlProvider.class,method = "insertSelective")
    void insertSelective(@Param(MysqlProvider.CLAZZ)Object entity);

    @DeleteProvider(type = MysqlProvider.class,method = "delete")
    void delete(@Param(MysqlProvider.CLAZZ)Object entity);

    @UpdateProvider(type = MysqlProvider.class,method = "update")
    void update(@Param(MysqlProvider.CLAZZ)Object entity);

    @SelectProvider(type = MysqlProvider.class,method = "selectById")
    <T> T selectById(@Param(MysqlProvider.ID_VALUE) Serializable id,@Param(MysqlProvider.CLAZZ) Class<T> tClass);

    @SelectProvider(type = MysqlProvider.class,method = "selectListByIds")
    <T> List<T> selectListByIds(@Param(MysqlProvider.PARAM_LIST) List<Object> idList,
                            @Param(MysqlProvider.CLAZZ) Class<T> clazz,
                            @Param(MysqlProvider.PAGINATION) Pagination pagination) ;

}
