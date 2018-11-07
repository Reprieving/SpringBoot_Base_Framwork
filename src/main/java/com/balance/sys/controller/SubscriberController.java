package com.balance.sys.controller;

import com.balance.core.dto.Pagination;
import com.balance.core.dto.Result;
import com.balance.core.service.BaseService;
import com.balance.sys.entity.Subscriber;
import com.balance.sys.service.SubscriberService;
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
