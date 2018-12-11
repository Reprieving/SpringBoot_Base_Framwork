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
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Result<?> list(@RequestBody Subscriber subscriber) throws Exception {
        Pagination pagination = subscriber.getPagination();
        Map<String, Object> whereMap = new HashMap<>();
        if (StringUtils.isNoneBlank(subscriber.getUserName())) {
            whereMap = ImmutableMap.of(Subscriber.User_name + " like ", "%" + subscriber.getUserName() + "%");
        }
        List<Subscriber> subscriberList = subscriberService.selectListByWhereMap(whereMap, pagination, Subscriber.class);

        Integer count = pagination == null ? subscriberList.size() : pagination.getTotalRecordNumber();
        return ResultUtils.success(subscriberList, count);
    }

    @RequestMapping("login")
    public Result<?> login(HttpServletRequest request, @RequestBody Subscriber subscriber) throws Exception {
        String subscriberId=null;
        if(StringUtils.isNoneBlank(subscriber.getLoginToken())){
            subscriberId = JwtUtils.getSubscriberByToken(subscriber.getLoginToken()).getId();
        }

        Subscriber subscriberPo;
        if (StringUtils.isNoneBlank(subscriberId)) {
            subscriberPo = subscriberService.selectOneById(subscriberId, Subscriber.class);
        } else {
            subscriberPo = subscriberService.getSubscriberByLogin(subscriber.getUserName(), subscriber.getPassword());
        }

        if (subscriberPo == null) {
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
