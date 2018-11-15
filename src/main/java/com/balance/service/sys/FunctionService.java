package com.balance.service.sys;

import com.balance.architecture.mybatis.mapper.BaseMapper;
import com.balance.entity.sys.FuncTreeNode;
import com.balance.entity.sys.Function;
import com.balance.mapper.sys.FunctionMapper;
import com.balance.architecture.utils.TreeNodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FunctionService {

    @Autowired
    private FunctionMapper functionMapper;

    @Autowired
    private BaseMapper baseMapper;

    public FuncTreeNode queryFuncTree(String subscriberId) {
        List<FuncTreeNode> functionList = functionMapper.selectFuncList(subscriberId);
        FuncTreeNode funcTreeNode = TreeNodeUtils.generateFucTreeNode("0",functionList);
        return funcTreeNode;
    }

    public void createFunc(Function function) {
        function.setId(UUID.randomUUID().toString().replace("-",""));
        functionMapper.insertFuc(function);
    }

    public Function viewFuc(Function function){
        Function f = baseMapper.selectById(function.getId(),Function.class);
        return f;
    }

}
