package com.balance.sys.controller;

import com.balance.core.controller.BaseController;
import com.balance.core.dto.Result;
import com.balance.sys.entity.FuncTreeNode;
import com.balance.sys.entity.Function;
import com.balance.sys.entity.Subscriber;
import com.balance.sys.service.FunctionService;
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
    public Result<?> viewFunc(Function function) throws Exception {
        try{
            return ResultUtils.success(functionService.viewFuc(function),"success");
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("Server Error");
        }

    }
}
