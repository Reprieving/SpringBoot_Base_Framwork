package com.balance.service.User;

import com.balance.architecture.exception.BussinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.user.User;
import com.balance.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService{
    @Autowired
    private UserMapper userMapper;

    public void createUser(User user) throws BussinessException {
        User user1 = selectOneByWhere("user_name=",user.getUserName(),User.class);
        if(user1!=null){
            throw new BussinessException("用户已存在");
        }
        super.insertIfNotNull(user);
    }
}
