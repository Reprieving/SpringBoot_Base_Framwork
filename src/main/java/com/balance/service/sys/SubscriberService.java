package com.balance.service.sys;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.sys.Role;
import com.balance.entity.sys.Subscriber;
import com.balance.entity.sys.SubscriberRole;
import com.balance.mapper.sys.SubscriberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SubscriberService extends BaseService{

    @Autowired
    private SubscriberMapper subscriberMapper;

    public Integer deleteRoles(String subscriberId){
        return subscriberMapper.deleteRoles(subscriberId);
    }

    public void insertSubscriber(Subscriber subscriber){
        List<SubscriberRole> subscriberRoles = new ArrayList<>();
        Subscriber subscriberPo = selectOneByWhereString(Subscriber.User_name+"=",subscriber.getUserName(),Subscriber.class);
        if(subscriberPo!=null){
            throw new BusinessException("账号已存在");
        }

        insertIfNotNull(subscriber);
        for(String roleId:subscriber.getRoleIdList()){
            subscriberRoles.add(new SubscriberRole(subscriber.getId(),roleId));
        }
        insertBatch(subscriberRoles,false);
    }

    public void updateSubscriber(Subscriber subscriber){
        String subscriberId = subscriber.getId();
        Subscriber subscriberPo = selectOneById(subscriberId,Subscriber.class);
        if(subscriberPo ==null){
            throw new BusinessException("未找到用户");
        }
        deleteRoles(subscriberId);

        List<SubscriberRole> subscriberRoles = new ArrayList<>();
        for(String roleId:subscriber.getRoleIdList()){
            subscriberRoles.add(new SubscriberRole(subscriber.getId(),roleId));
        }
        insertBatch(subscriberRoles,false);

    }


    public Subscriber getSubscriberByLogin(String userName, String password) {
        return subscriberMapper.selectSubscriberByLogin(userName, password);
    }

    public Subscriber listRole(String subscriberId) {
        return subscriberMapper.listRole(subscriberId);
    }
}
