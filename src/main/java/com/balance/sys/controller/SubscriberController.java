package com.balance.sys.controller;

import com.balance.core.controller.BaseController;
import com.balance.core.dto.Pagination;
import com.balance.core.dto.Result;
import com.balance.sys.entity.Subscriber;
import com.balance.sys.specs.service.SubscriberService;
import com.balance.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("subscriber")
public class SubscriberController extends BaseController{

    @Autowired
    private SubscriberService subscriberService;

    @RequestMapping("add")
    public Object add(Subscriber subscriber){
        subscriberService.insertOne(subscriber);
        return subscriber;
    }

    @RequestMapping("list")
    public Result<?> list(Subscriber subscriber) throws Exception{
        Pagination pagination = new Pagination();
        subscriber.setId("5");
        List<Subscriber> list = subscriberService.selectList(pagination,subscriber);

        return ResultUtils.success(list,"success");
    }




}
