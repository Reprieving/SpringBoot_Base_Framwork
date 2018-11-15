package com.balance.controller.admin.sys;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.entity.sys.Subscriber;
import com.balance.service.sys.FunctionService;
import com.balance.service.sys.SubscriberService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin/subscriber")
public class AdminSubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private FunctionService functionService;

    @Autowired
    private BaseService baseService;

    @RequestMapping("add")
    public Result<?> add(Subscriber subscriber) throws Exception {
        baseService.insert(subscriber);
        return ResultUtils.success("success");
    }

    @RequestMapping("list")
    public Result<?> list(Subscriber subscriber) throws Exception{
        Pagination pagination = new Pagination();
        subscriber.setId("5");
        List<Subscriber> list = subscriberService.selectList(pagination,subscriber);

        pagination.setObjectList(list);

        return ResultUtils.success(pagination,"success");
    }

    @RequestMapping("login")
    public Result<?> create(Subscriber subscriber) throws Exception {
        Subscriber subscriberPo = subscriberService.getSubscriberByLogin(subscriber.getUserName(),subscriber.getPassword());
        if(subscriberPo == null){
            return ResultUtils.error("用户名或密码错误");
        }
        subscriberPo.setFuncTreeNode(functionService.queryFuncTree(subscriberPo.getId()));
        subscriberPo.setLoginToken(JwtUtils.createToken(subscriberPo));
        return ResultUtils.success(subscriberPo,"success");
    }



}
