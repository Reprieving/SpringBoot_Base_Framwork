package com.balance.service.work;

import com.balance.utils.ParamStatementUtils;
import com.balance.utils.TreeNodeUtils;
import com.balance.utils.UUIDUtils;
import com.balance.entity.work.ApiTreeNode;
import com.balance.entity.work.AppInterface;
import com.balance.entity.work.ParamStatement;
import com.balance.entity.work.Project;
import com.balance.mapper.work.AppInterfaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppInterfaceService {

    @Autowired
    private AppInterfaceMapper appInterfaceMapper;


    public void createInterface(AppInterface appInterface) {
        appInterface.setId(UUIDUtils.createUUID());
        appInterfaceMapper.insertAppInterface(appInterface);
        List<ParamStatement> paramStatementList = ParamStatementUtils.buildParamStatementList(appInterface);
        if(paramStatementList.size()>0){
            appInterfaceMapper.insertParamStatement(paramStatementList);
        }
    }

    public ApiTreeNode queryInterfacesByProject(Project project) throws Exception {
        List<ApiTreeNode> appInterfaceList = appInterfaceMapper.selectInterfaces4Tree(project);
        List<ApiTreeNode> projectRootList = appInterfaceList.stream()
                .filter(f -> f.getIsProjectRoot())
                .collect(Collectors.toList());
        if (projectRootList.size() != 1) {
            throw new Exception("obj error");
        }

        return TreeNodeUtils.generateApiTreeNode(projectRootList.get(0)
                .getId(), appInterfaceList);
    }

    public AppInterface queryInterfaceById(AppInterface appInterface){
        AppInterface appInterfacePo = appInterfaceMapper.selectInterfaceById(appInterface);
        ParamStatementUtils.filterReqRspParamStatement(appInterfacePo);
        return appInterfacePo;
    }

    public void updateInterfaceById(AppInterface appInterface) {
        appInterfaceMapper.updateAppInterfaceById(appInterface);
        appInterfaceMapper.deleteAppInterfaceById(appInterface);
        List<ParamStatement> paramStatementList = ParamStatementUtils.buildParamStatementList(appInterface);
        if(paramStatementList.size()>0){
            appInterfaceMapper.insertParamStatement(paramStatementList);
        }
    }
}
