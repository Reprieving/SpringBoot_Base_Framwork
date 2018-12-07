package com.balance.controller.admin;

import com.balance.architecture.dto.Result;
import com.balance.entity.sys.FuncTreeNode;
import com.balance.entity.sys.Function;
import com.balance.entity.sys.Subscriber;
import com.balance.service.sys.FunctionService;
import com.balance.architecture.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/function")
public class AdminFunctionController {
    @Autowired
    private FunctionService functionService;

    @RequestMapping("subsFuncTree")
    public FuncTreeNode querySubsFuncTree(Subscriber subscriber){
        FuncTreeNode funcTreeNode = functionService.queryFuncTree(subscriber.getId());
        return funcTreeNode;
    }

    @RequestMapping("allFuncTree")
    public FuncTreeNode queryFuncTree(){
        FuncTreeNode funcTreeNode = functionService.queryFuncTree("0");
        return funcTreeNode;
    }

    @RequestMapping("create")
    public Result<?> createFunc(@RequestBody Function function){
        functionService.insertIfNotNull(function);
        return ResultUtils.success("创建接口成功");
    }

    @RequestMapping("info/{funcId}")
    public Result<?> viewFunc(@PathVariable String funcId)  {
        return ResultUtils.success(functionService.selectOneById(funcId,Function.class));
    }

    @RequestMapping("delete/{funcId}")
    public Result<?> deleteFunc(@PathVariable String funcId){
        functionService.deleteFuc(funcId);
        return ResultUtils.success("删除成功");
    }

    @RequestMapping("update")
    public Result<?> updateFunc(@RequestBody Function function){
        functionService.updateIfNotNull(function);
        return ResultUtils.success("更新成功");
    }

}
