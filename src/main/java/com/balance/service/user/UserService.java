package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.UserConst;
import com.balance.entity.common.UserFreeCount;
import com.balance.entity.common.UserInviteCodeId;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.entity.user.UserFrozenAssets;
import com.balance.mapper.common.AutoIncreaseIdMapper;
import com.balance.mapper.user.UserMapper;
import com.balance.service.common.AliOSSBusiness;
import com.balance.utils.EncryptUtils;
import com.balance.utils.RandomUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService extends BaseService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AutoIncreaseIdMapper autoIncreaseIdMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AliOSSBusiness aliOSSBusiness;

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
                ValueCheckUtils.notEmpty(user.getPassword(), "密码不能为空");
                ValueCheckUtils.notEmpty(user.getPhoneNumber(), "手机号码不能为空");

                User user1 = selectOneByWhereString(User.Phone_number + " = ", user.getPhoneNumber(), User.class);
                if (user1 != null) {
                    throw new BusinessException("用户已存在");
                }

                User inviteUser = selectOneByWhereString(User.Invite_code + " = ", user.getInviteCode(), User.class);
                ValueCheckUtils.notEmpty(inviteUser, "邀请码对应的用户不存在");

                user.setInviteId(inviteUser.getId());
                user.setUserName("");
                user.setCreateTime(new Timestamp(System.currentTimeMillis()));

                //生成邀请码
                UserInviteCodeId userInviteCodeId = new UserInviteCodeId();
                autoIncreaseIdMapper.insertUserInviteCode(userInviteCodeId);
                String newInviteCode = RandomUtil.randomInviteCode(userInviteCodeId.getId());

                user.setInviteCode(newInviteCode);
                user.setPassword(EncryptUtils.md5Password(user.getPassword()));

                insert(user);

                String userId = user.getId();

                //增加用户冻结资产记录
                UserAssets userAssets = new UserAssets();
                userAssets.setUserId(userId);
                insertIfNotNull(userAssets);

                //增加用户冻结资产记录
                UserFrozenAssets userFrozenAssets = new UserFrozenAssets();
                userFrozenAssets.setUserId(userId);
                insertIfNotNull(userFrozenAssets);

                //增加用户每天免费次数记录
                UserFreeCount userFreeCount = new UserFreeCount();
                userFreeCount.setUser_id(userId);
                insertIfNotNull(userFreeCount);
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
        paramMap.put(User.Phone_number + "=", user.getPhoneNumber());
        paramMap.put(User.Password + "=", user.getPassword());

        User user1 = selectOneByWhereMap(paramMap, User.class);
        if (user == null) {
            throw new BusinessException("账号或密码有误");
        }


        User user2 = new User();
        user2.setUserName(user1.getUserName());
        user2.setHeadPictureUrl(user1.getHeadPictureUrl());
        user2.setAccessToken(JwtUtils.createToken(user1));

        return user2;
    }


    /**
     * 修改头像
     *
     * @param userId 用户id
     * @param file   头像图片
     */
    public String updateHeadPic(String userId, MultipartFile file) {
        String fileDirectory = DateFormatUtils.format(new Date(), "yyyy-MM-dd|HH");
        String imgUrl = aliOSSBusiness.uploadCommonPic(file, fileDirectory);

        User user = new User();
        user.setId(userId);
        user.setHeadPictureUrl(imgUrl);

        Integer i = updateIfNotNull(user);
        if (i == 0) {
            throw new BusinessException("上传头像失败");
        }
        return imgUrl;
    }

    /**
     * 修改用户名
     */
    public void updateUserName(String userId, String userName) {
        ValueCheckUtils.notEmpty(userName, "用户昵称不能为空");
        User userPo = selectOneByWhereString(User.User_name + "=", userName, User.class);
        if (userPo != null) {
            throw new BusinessException("用户昵称已存在");
        }
        User user = new User();
        user.setId(userId);
        user.setUserName(userName);
        Integer i = updateIfNotNull(user);
        if (i == 0) {
            throw new BusinessException("修改用户昵称失败");
        }
    }


    /**
     * 获取所有用户（用于展示邀请记录）
     * @return
     */
    public List<User> listUser4InviteRecord(){
        return userMapper.listUser4InviteRecord();
    }

    /**
     * 查询用户邀请记录
     *
     * @param userId 用户id
     * @return
     */
    @Cacheable(value = "listInviteUser",sync = true)
    public User listInviteUser(String userId) {
        List<User> allUser = listUser4InviteRecord();

        //直接邀请用户列表
        List<User> directUserList = new ArrayList<>();
        for (Iterator it = allUser.iterator(); it.hasNext(); ) {
            User user = (User) it.next();
            if (user.getInviteId().equals(userId)) {
                directUserList.add(user);
                it.remove();
            }
        }

        //间接邀请用户列表
        List<User> inDirectUserList = new ArrayList<>();
        for (User user1 : allUser) {
            for (User user2 : directUserList) {
                if (user1.getInviteId().equals(user2.getId())) {
                    inDirectUserList.add(user1);
                }
            }
        }

        return new User(directUserList, inDirectUserList);
    }


    /**
     * 根据短信类型重置密码
     *
     * @param userId      用户id
     * @param newPassword 新密码
     * @param msgType     短信类型
     */
    public void resetPassword(String userId, String newPassword, Integer msgType) {
        ValueCheckUtils.notEmpty(newPassword, "新密码不能为空");
        String updatePWDColumn;
        if (UserConst.MSG_CODE_TYPE_RESET_LOGINPWD == msgType) {
            updatePWDColumn = User.Password;
        } else if (UserConst.MSG_CODE_TYPE_RESET_PAYPWD == msgType) {
            updatePWDColumn = User.Pay_password;
        } else {
            throw new BusinessException("短信类型有误");
        }

        newPassword = EncryptUtils.md5Password(newPassword);
        ValueCheckUtils.notEmpty(newPassword, "新密码字符串异常");

        Integer i = userMapper.updatePassword(userId, newPassword, updatePWDColumn);
        if (i == 0) {
            throw new BusinessException("设置密码失败");
        }

    }

    /**
     * 修改密码
     *
     * @param userId        用户Id
     * @param oldPassword   旧密码
     * @param newPassword   新密码
     * @param updatePwdType 修改类型
     */
    public void updatePassword(String userId, String phoneNumber, String oldPassword, String newPassword, Integer updatePwdType) {
        ValueCheckUtils.notEmpty(phoneNumber, "手机号不能为空");
        ValueCheckUtils.notEmpty(newPassword, "新密码不能为空");
        ValueCheckUtils.notEmpty(oldPassword, "旧密码不能为空");
        ValueCheckUtils.notEmpty(updatePwdType, "修改密码类型不能为空");

        String updatePWDColumn;
        if (UserConst.UPDATE_PWD_TYPE_LOGIN == updatePwdType) {
            updatePWDColumn = User.Password;
        } else if (UserConst.UPDATE_PWD_TYPE_PAY == updatePwdType) {
            updatePWDColumn = User.Pay_password;
        } else {
            throw new BusinessException("修改密码类型有误");
        }

        oldPassword = EncryptUtils.md5Password(oldPassword);

        User user = userMapper.getUserToUpdatePwd(phoneNumber, updatePWDColumn, oldPassword);
        ValueCheckUtils.notEmpty(user, "密码有误");

        Integer i = userMapper.updatePassword(userId, newPassword, updatePWDColumn);
        if (i == 0) {
            throw new BusinessException("修改密码失败");
        }
    }



}
