package com.balance.mapper.work;

import com.balance.entity.work.ApiTreeNode;
import com.balance.entity.work.AppInterface;
import com.balance.entity.work.ParamStatement;
import com.balance.entity.work.Project;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppInterfaceMapper{
    List<ApiTreeNode> selectInterfaces4Tree(@Param("project") Project project);

    void insertAppInterface(@Param("appInterface")AppInterface appInterface);

    void insertParamStatement(@Param("paramStatementList")List<ParamStatement> paramStatementList);

    AppInterface selectInterfaceById(@Param("appInterface") AppInterface appInterface);

    void updateAppInterfaceById(@Param("appInterface")AppInterface appInterface);

    void deleteAppInterfaceById(@Param("appInterface")AppInterface appInterface);
}
