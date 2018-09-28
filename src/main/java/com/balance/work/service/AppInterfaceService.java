package com.balance.work.service;

import com.balance.utils.ParamStatementUtils;
import com.balance.utils.TreeNodeUtils;
import com.balance.utils.UUIDUtils;
import com.balance.work.Exception.DataErrorException;
import com.balance.work.entity.ApiTreeNode;
import com.balance.work.entity.AppInterface;
import com.balance.work.entity.ParamStatement;
import com.balance.work.entity.Project;
import com.balance.work.mapper.AppInterfaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppInterfaceService {

    @Autowired
    private AppInterfaceMapper appInterfaceMapper;


    public void createInterface(AppInterface appInterface) throws SQLException {
        appInterface.setId(UUIDUtils.createUUID());
        appInterfaceMapper.insertAppInterface(appInterface);
        List<ParamStatement> paramStatementList = ParamStatementUtils.buildParamStatementList(appInterface);
        if(paramStatementList.size()>0){
            appInterfaceMapper.insertParamStatement(paramStatementList);
        }
    }

    public ApiTreeNode queryInterfacesByProject(Project project) throws SQLException, DataErrorException {
        List<ApiTreeNode> appInterfaceList = appInterfaceMapper.selectInterfaces4Tree(project);
        List<ApiTreeNode> projectRootList = appInterfaceList.stream()
                .filter(f -> f.getIsProjectRoot())
                .collect(Collectors.toList());
        if (projectRootList.size() != 1) {
            throw new DataErrorException("obj error");
        }

        return TreeNodeUtils.generateApiTreeNode(projectRootList.get(0)
                .getId(), appInterfaceList);
    }

    public AppInterface queryInterfaceById(AppInterface appInterface) throws SQLException {
        AppInterface appInterfacePo = appInterfaceMapper.selectInterfaceById(appInterface);
        ParamStatementUtils.filterReqRspParamStatement(appInterfacePo);
        return appInterfacePo;
    }

    public void updateInterfaceById(AppInterface appInterface) throws SQLException{
        appInterfaceMapper.updateAppInterfaceById(appInterface);
        appInterfaceMapper.deleteAppInterfaceById(appInterface);
        List<ParamStatement> paramStatementList = ParamStatementUtils.buildParamStatementList(appInterface);
        if(paramStatementList.size()>0){
            appInterfaceMapper.insertParamStatement(paramStatementList);
        }
    }
}
