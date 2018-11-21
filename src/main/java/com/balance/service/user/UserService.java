package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.entity.user.User;
import com.balance.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService extends BaseService{
    @Autowired
    private UserMapper userMapper;

    /**
     * 注册用户
     * @param user
     * @throws BusinessException
     */
    public void createUser(User user) throws BusinessException {
        User user1 = selectOneByWhereString("user_name=",user.getUserName(),User.class);
        if(user1!=null){
            throw new BusinessException("用户已存在");
        }
        insertIfNotNull(user);
    }

    /**
     * 用户登录
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    public User login(User user) throws UnsupportedEncodingException {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("phone_number=",user.getPhoneNumber());
        paramMap.put("password=",user.getPassword());

        User user1 = selectOneByWhereMap(paramMap,User.class);

        User user2 = new User();
        user2.setUserName(user1.getUserName());
        user2.setHeadPictureUrl(user1.getHeadPictureUrl());
        user2.setAccessToken(JwtUtils.createToken(user1));

        return user2;
    }
}
