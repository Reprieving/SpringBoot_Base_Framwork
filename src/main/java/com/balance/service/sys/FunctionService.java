package com.balance.service.sys;

import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.entity.sys.FuncTreeNode;
import com.balance.entity.sys.Function;
import com.balance.mapper.sys.FunctionMapper;
import com.balance.architecture.utils.TreeNodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class FunctionService extends BaseService{

    @Autowired
    private FunctionMapper functionMapper;

    public FuncTreeNode queryFuncTree(String subscriberId) {
        List<FuncTreeNode> functionList = functionMapper.selectFuncList(subscriberId);
        FuncTreeNode funcTreeNode = TreeNodeUtils.generateFucTreeNode("0",functionList);
        return funcTreeNode;
    }


    public void deleteFuc(String fucId){
        Function function = selectOneById(fucId,Function.class);
        ValueCheckUtils.notEmpty(function,"未找到记录");
        delete(function);
    }
}
