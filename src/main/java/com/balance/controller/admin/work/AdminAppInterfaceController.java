package com.balance.controller.admin.work;

import com.alibaba.fastjson.JSON;
import com.balance.architecture.dto.Result;
import com.balance.architecture.utils.ResultUtils;
import com.balance.entity.work.ApiTreeNode;
import com.balance.entity.work.AppInterface;
import com.balance.entity.work.Project;
import com.balance.service.work.AppInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("admin/appInterface")
@CrossOrigin
public class AdminAppInterfaceController {

    @Autowired
    private AppInterfaceService appInterfaceService;

    @RequestMapping("create")
    public Result<?> createInterface(String dataStr) {
        AppInterface appInterface = JSON.parseObject(dataStr, AppInterface.class);
        appInterfaceService.createInterface(appInterface);
        return ResultUtils.success(null, "Create Success");
    }

    @RequestMapping("interfaces")
    public Result<?> queryInterfaces(Project project) throws Exception {
        ApiTreeNode apiTreeNode = appInterfaceService.queryInterfacesByProject(project);
        return ResultUtils.success(apiTreeNode, "Query Success");
    }

    @RequestMapping("update")
    public Result<?> queryInterfaces(String dataStr) throws SQLException {
        AppInterface appInterface = JSON.parseObject(dataStr, AppInterface.class);
        appInterfaceService.updateInterfaceById(appInterface);
        return ResultUtils.success(null, "Update Success");
    }

    @RequestMapping("interfaceInfo")
    public Result<?> queryInterfaceInfo(AppInterface appInterfaceVo) {
        AppInterface appInterface = appInterfaceService.queryInterfaceById(appInterfaceVo);
        return ResultUtils.success(appInterface, "Query Success");
    }
}
