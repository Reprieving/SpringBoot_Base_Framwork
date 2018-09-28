package com.balance.sys.specs.service;

import com.balance.core.dto.Pagination;
import com.balance.sys.entity.Subscriber;
import com.balance.sys.mapper.FunctionMapper;
import com.balance.sys.mapper.SubscriberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriberService {
    private final static Logger logger = LoggerFactory.getLogger(SubscriberService.class);

    @Autowired
    private SubscriberMapper subscriberMapper;

    @Autowired
    private FunctionMapper functionMapper;


    @Transactional
    public void insertOne(Subscriber subscriber) {
        subscriber.setId(UUID.randomUUID().toString().replace("-",""));
        subscriber.setUserName("admin");
        subscriber.setRealName("lee");
        subscriber.setPassword("admin");
        subscriber.setDepartmentId("1");
        subscriber.setWorkNumber("1");
        subscriber.setCreateBy("system");
        subscriber.setCreateTime(new Timestamp(System.currentTimeMillis()));
        subscriberMapper.insertUser(subscriber);
    }


    public List<Subscriber> selectList(Pagination pagination, Subscriber subscriber) {
        return  subscriberMapper.selectList(pagination, subscriber);
    }

}
