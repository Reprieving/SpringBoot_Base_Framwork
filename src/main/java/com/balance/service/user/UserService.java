package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
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
//        User user1 = selectOneByWhereString("user_name=",user.getUserName(),User.class);
        User user1 = selectOneById("2d201a2677d34f608cae175a53599882", User.class);
        user1 = selectOneByWhereString("id=","2d201a2677d34f608cae175a535998821",User.class);
        Map<String,Object> map = new HashMap<>();
        map.put("id=","2d201a2677d34f608cae175a53599882");
        map.put("password=","1");
        user1 = selectOneByWhereMap(map,User.class);
//        if(user1!=null){
//            throw new BusinessException("用户已存在");
//        }
//        insertIfNotNull(user);
        List<User> userList = selectAll(User.class,new Pagination());
    }

    /**
     * 用户登录
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    public String login(User user) throws UnsupportedEncodingException {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("user_name=",user.getUserName());
        paramMap.put("password=",user.getPassword());

        User user1 = selectOneByWhereMap(paramMap,User.class);

        return JwtUtils.createToken(user1);
    }
}
