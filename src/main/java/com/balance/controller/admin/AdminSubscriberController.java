package com.balance.controller.admin;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.entity.sys.Role;
import com.balance.entity.sys.Subscriber;
import com.balance.entity.sys.SubscriberRole;
import com.balance.service.sys.FunctionService;
import com.balance.service.sys.RoleService;
import com.balance.service.sys.SubscriberService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("admin/subscriber")
public class AdminSubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private FunctionService functionService;


    @RequestMapping("create")
    public Result<?> create(@RequestBody Subscriber subscriber) throws Exception {
        subscriberService.insertSubscriber(subscriber);
        return ResultUtils.success("创建用户成功");
    }

    @RequestMapping("list")
    public Result<?> list(@RequestBody Subscriber subscriber) throws Exception{
        Pagination pagination = subscriber.getPagination();
        List<Subscriber> subscriberList;
        if (StringUtils.isNoneBlank(subscriber.getUserName())) {
            subscriberList = subscriberService.selectListByWhereString(Subscriber.User_name + " like ", "%" + subscriber.getUserName() + "%", pagination, Subscriber.class);
        } else {
            subscriberList = subscriberService.selectAll(pagination, Subscriber.class);
        }
        Integer count = pagination == null ? subscriberList.size() : pagination.getTotalRecordNumber();
        return ResultUtils.success(subscriberList,count);
    }

    @RequestMapping("login")
    public Result<?> login(@RequestBody Subscriber subscriber) throws Exception {
        Subscriber subscriberPo = subscriberService.getSubscriberByLogin(subscriber.getUserName(),subscriber.getPassword());
        if(subscriberPo == null){
            return ResultUtils.error("用户名或密码错误");
        }
        subscriberPo.setFuncTreeNode(functionService.queryFuncTree(subscriberPo.getId()));
        subscriberPo.setLoginToken(JwtUtils.createToken(subscriberPo));
        return ResultUtils.success(subscriberPo);
    }

    @RequestMapping("edit")
    public Result<?> edit(@RequestBody Subscriber subscriberReq) throws Exception {
        Subscriber subscriber = subscriberService.listRole(subscriberReq.getId());
        return ResultUtils.success(subscriber);
    }

    @RequestMapping("update")
    public Result<?> update(@RequestBody Subscriber subscriberReq) throws Exception {
        subscriberService.updateSubscriber(subscriberReq);
        return ResultUtils.success("保存成功");
    }
}
