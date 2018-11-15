package com.balance.service.sys;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.sys.Subscriber;
import com.balance.mapper.sys.SubscriberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriberService {

    @Autowired
    private SubscriberMapper subscriberMapper;

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

    public Subscriber getSubscriberByLogin(String userName, String password) {
        return subscriberMapper.selectSubscriberByLogin(userName, password);

    }
}
