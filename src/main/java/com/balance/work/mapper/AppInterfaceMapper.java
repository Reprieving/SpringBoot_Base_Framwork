package com.balance.work.mapper;

import com.balance.core.mybatis.mapper.BaseMapper;
import com.balance.work.entity.ApiTreeNode;
import com.balance.work.entity.AppInterface;
import com.balance.work.entity.ParamStatement;
import com.balance.work.entity.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppInterfaceMapper extends BaseMapper{
    List<ApiTreeNode> selectInterfaces4Tree(@Param("project") Project project);

    void insertAppInterface(@Param("appInterface")AppInterface appInterface);

    void insertParamStatement(@Param("paramStatementList")List<ParamStatement> paramStatementList);

    AppInterface selectInterfaceById(@Param("appInterface") AppInterface appInterface);

    void updateAppInterfaceById(@Param("appInterface")AppInterface appInterface);

    void deleteAppInterfaceById(@Param("appInterface")AppInterface appInterface);
}
