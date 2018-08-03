package com.balance.sys.controller;

import com.balance.core.controller.BaseController;
import com.balance.core.dto.Pagination;
import com.balance.core.dto.Result;
import com.balance.sys.entity.Subscriber;
import com.balance.sys.specs.SubscriberSpecs;
import com.balance.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("subscriber")
public class SubscriberController extends BaseController{

    @Autowired
    private SubscriberSpecs subscriberSpecs;

    @RequestMapping("add")
    public Object add(Subscriber subscriber){
        subscriberSpecs.insertOne(subscriber);
        return subscriber;
    }

    @RequestMapping("list")
    public Result<?> list(Subscriber subscriber) throws Exception{
        Pagination pagination = new Pagination();
        subscriber.setId("5");
        List<Subscriber> list = subscriberSpecs.selectList(pagination,subscriber);

        return ResultUtils.success(list,"success");
    }




}
