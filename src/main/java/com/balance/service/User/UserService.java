package com.balance.service.User;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BussinessException;
import com.balance.architecture.service.BaseService;
import com.balance.entity.user.User;
import com.balance.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService{
    @Autowired
    private UserMapper userMapper;

    public void createUser(User user) throws BussinessException {
        List<User> user1 = selectListByWhere("user_name=",user.getUserName(),User.class,null);
        if(user1!=null){
            throw new BussinessException("用户已存在");
        }
//        super.insertIfNotNull(user);
    }

    public String login(User user) {
        Map<String,Object> paramMap = new HashMap<>();
        user.setUserName("1");
        user.setPassword("1");
        paramMap.put("user_name=",user.getUserName());
        paramMap.put("password=",user.getPassword());

        List<User> user1 = selectListByWhereMap(paramMap,User.class,new Pagination());
//        User user1 = userMapper.


        return null;
    }
}
