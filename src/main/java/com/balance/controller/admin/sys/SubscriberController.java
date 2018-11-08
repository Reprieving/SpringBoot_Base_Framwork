package com.balance.controller.admin.sys;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.dto.Result;
import com.balance.architecture.service.BaseService;
import com.balance.entity.sys.Subscriber;
import com.balance.service.sys.SubscriberService;
import com.balance.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("subscriber")
public class SubscriberController{

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private BaseService baseService;

    @RequestMapping("add")
    public Object add(Subscriber subscriber) throws Exception {
        baseService.insert(subscriber);
        return subscriber;
    }

    @RequestMapping("list")
    public Result<?> list(Subscriber subscriber) throws Exception{
        Pagination pagination = new Pagination();
        subscriber.setId("5");
        List<Subscriber> list = subscriberService.selectList(pagination,subscriber);

        pagination.setObjectList(list);

        return ResultUtils.success(pagination,"success");
    }




}
