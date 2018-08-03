package com.balance.work.specs;

import com.balance.work.Exception.DataErrorException;
import com.balance.work.entity.ApiTreeNode;
import com.balance.work.entity.AppInterface;
import com.balance.work.entity.Project;
import org.apache.ibatis.jdbc.SQL;

import java.sql.SQLException;

public interface AppInterfaceSpecs {
    void createInterface(AppInterface appInterface) throws SQLException;

    ApiTreeNode queryInterfacesByProject(Project project) throws SQLException, DataErrorException;

    AppInterface queryInterfaceById(AppInterface appInterface) throws SQLException;

    void updateInterfaceById(AppInterface appInterface) throws SQLException;
}
