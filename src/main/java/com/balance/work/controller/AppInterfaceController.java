package com.balance.work.controller;

import com.alibaba.fastjson.JSON;
import com.balance.core.dto.Result;
import com.balance.core.specs.BaseSpecs;
import com.balance.utils.ResultUtils;
import com.balance.work.Exception.DataErrorException;
import com.balance.work.entity.ApiTreeNode;
import com.balance.work.entity.AppInterface;
import com.balance.work.entity.Project;
import com.balance.work.specs.AppInterfaceSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("appInterface")
@CrossOrigin
public class AppInterfaceController {

    @Autowired
    private AppInterfaceSpecs appInterfaceSpecs;

    @RequestMapping("create")
    public Result<?> createInterface(String dataStr) throws SQLException {
        try {
            AppInterface appInterface = JSON.parseObject(dataStr, AppInterface.class);
            appInterfaceSpecs.createInterface(appInterface);
            return ResultUtils.success(null, "Create Success");
        } catch (SQLException e) {
            throw new SQLException("Sql exception");
        }
    }

    @RequestMapping("interfaces")
    public Result<?> queryInterfaces(Project project) throws SQLException, DataErrorException {
        try {
            ApiTreeNode apiTreeNode = appInterfaceSpecs.queryInterfacesByProject(project);
            return ResultUtils.success(apiTreeNode, "Query Success");
        } catch (SQLException e) {
            throw new SQLException("Sql exception");
        }
    }

    @RequestMapping("update")
    public Result<?> queryInterfaces(String dataStr) throws SQLException, DataErrorException {
        try {
            AppInterface appInterface = JSON.parseObject(dataStr, AppInterface.class);
            appInterfaceSpecs.updateInterfaceById(appInterface);
            return ResultUtils.success(null, "Update Success");
        } catch (SQLException e) {
            throw new SQLException("Sql exception");
        }
    }

    @RequestMapping("interfaceInfo")
    public Result<?> queryInterfaceInfo(AppInterface appInterfaceVo) throws SQLException {
        try {
            AppInterface appInterface = appInterfaceSpecs.queryInterfaceById(appInterfaceVo);
            return ResultUtils.success(appInterface, "Query Success");
        } catch (SQLException e) {
            throw new SQLException("Sql exception");
        }
    }
}
