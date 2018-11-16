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


    public List<Subscriber> selectList(Pagination pagination, Subscriber subscriber) {
        return  subscriberMapper.selectList(pagination, subscriber);
    }

    public Subscriber getSubscriberByLogin(String userName, String password) {
        return subscriberMapper.selectSubscriberByLogin(userName, password);

    }
}
