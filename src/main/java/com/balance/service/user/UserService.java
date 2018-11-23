package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.entity.common.UserSendCount;
import com.balance.entity.user.MsgRecord;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.entity.user.UserFrozenAssets;
import com.balance.mapper.user.UserMapper;
import com.balance.utils.RandomUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.xml.crypto.Data;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 注册用户
     *
     * @param user
     * @throws BusinessException
     */
    public void createUser(User user) throws BusinessException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                if(null == user.getPassword()){
                    throw new BusinessException("密码不能为空");
                }

                if(null == user.getPhoneNumber()){
                    throw new BusinessException("手机号码不能为空");
                }

                User user1 = selectOneByWhereString("phone_number=", user.getPhoneNumber(), User.class);
                if (user1 != null) {
                    throw new BusinessException("用户已存在");
                }

                User inviteUser = selectOneByWhereString("invite_code=", user.getInviteCode(), User.class);
                if (inviteUser == null) {
                    throw new BusinessException("邀请码对应的用户不存在");
                }

                user.setInviteId(inviteUser.getId());
                user.setUserName("");
                user.setCreateTime(new Timestamp(System.currentTimeMillis()));

                while (true) {
                    Long timeId = Long.valueOf(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSS"));
                    String newInviteCode = RandomUtil.randomInviteCode(timeId);

                    User user3 = selectOneByWhereString("invite_code =", newInviteCode, User.class);
                    if (user3 == null) {
                        user.setInviteCode(newInviteCode);
                        break;
                    }
                }


                insert(user);

                String userId =user.getId();

                //增加用户冻结资产记录
                UserAssets userAssets = new UserAssets();
                userAssets.setUserId(userId);
                insertIfNotNull(userAssets);

                //增加用户冻结资产记录
                UserFrozenAssets userFrozenAssets = new UserFrozenAssets();
                userFrozenAssets.setUserId(userId);
                insertIfNotNull(userFrozenAssets);


                //增加用户发送服务使用次数记录
                UserSendCount userSendCount = new UserSendCount();
                userSendCount.setUser_id(userId);
                insertIfNotNull(userSendCount);



            }
        });


    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     * @throws UnsupportedEncodingException
     */
    public User login(User user) throws UnsupportedEncodingException {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("phone_number=", user.getPhoneNumber());
        paramMap.put("password=", user.getPassword());

        User user1 = selectOneByWhereMap(paramMap, User.class);

        User user2 = new User();
        user2.setUserName(user1.getUserName());
        user2.setHeadPictureUrl(user1.getHeadPictureUrl());
        user2.setAccessToken(JwtUtils.createToken(user1));

        return user2;
    }
}
