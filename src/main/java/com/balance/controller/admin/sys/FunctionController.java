package com.balance.controller.admin.sys;

import com.balance.architecture.dto.Result;
import com.balance.entity.sys.FuncTreeNode;
import com.balance.entity.sys.Function;
import com.balance.entity.sys.Subscriber;
import com.balance.service.sys.FunctionService;
import com.balance.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("function")
@CrossOrigin
public class FunctionController{
    @Autowired
    private FunctionService functionService;

    @RequestMapping("subsFuncTree")
    public FuncTreeNode querySubsFuncTree(Subscriber subscriber){
        FuncTreeNode funcTreeNode = functionService.queryFuncTree(subscriber.getId());
        return funcTreeNode;
    }

    @RequestMapping("allFuncTree")
    public FuncTreeNode queryFuncTree(){
        FuncTreeNode funcTreeNode = functionService.queryFuncTree("1");
        return funcTreeNode;
    }

    @RequestMapping("create")
    public Result<?> createFunc(Function function){
        functionService.createFunc(function);
        return ResultUtils.success(null,"success");
    }

    @RequestMapping("info")
    public Result<?> viewFunc(Function function)  {
        return ResultUtils.success(functionService.viewFuc(function),"success");
    }
}
