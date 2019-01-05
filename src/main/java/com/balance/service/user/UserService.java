package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.service.common.GlobalConfigService;
import com.balance.utils.ValueCheckUtils;
import com.balance.client.RedisClient;
import com.balance.constance.*;
import com.balance.entity.common.UserFreeCount;
import com.balance.entity.common.UserInviteCodeId;
import com.balance.entity.user.*;
import com.balance.mapper.common.AutoIncreaseIdMapper;
import com.balance.mapper.user.UserMapper;
import com.balance.service.common.AddressService;
import com.balance.service.common.AliOSSBusiness;
import com.balance.utils.EncryptUtils;
import com.balance.utils.RandomUtil;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
public class UserService extends BaseService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AutoIncreaseIdMapper autoIncreaseIdMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AliOSSBusiness aliOSSBusiness;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private UserSendService userSendService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private GlobalConfigService globalConfigService;

    /**
     * 注册用户
     *
     * @param user
     * @throws BusinessException
     */
    public Map<String, String> createUser(User user) throws BusinessException {
        User userPo = selectOneByWhereString(User.Phone_number + "=", user.getPhoneNumber(), User.class);
        if (userPo != null) {
            throw new BusinessException("该手机号已注册");
        }

        return transactionTemplate.execute(new TransactionCallback<Map<String, String>>() {
            @Nullable
            @Override
            public Map<String, String> doInTransaction(TransactionStatus transactionStatus) {
                String inviteCode = user.getInviteCode();
                if (StringUtils.isNoneBlank(inviteCode)) {
                    User inviteUser = selectOneByWhereString(User.Invite_code + "=", inviteCode, User.class);
                    if (inviteUser != null) {
                        user.setInviteId(inviteUser.getId());
                    }
                }

                user.setUserName("");
                user.setCreateTime(new Timestamp(System.currentTimeMillis()));

                //生成邀请码
                UserInviteCodeId userInviteCodeId = new UserInviteCodeId();
                autoIncreaseIdMapper.insertUserInviteCode(userInviteCodeId);
                String newInviteCode = RandomUtil.randomInviteCode(userInviteCodeId.getId());

                user.setInviteCode(newInviteCode);

                insertIfNotNull(user);

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

                //跳转URL
                return ImmutableMap.of("redirectUrl", globalConfigService.get(GlobalConfigService.Enum.REDIRECT_AFTER_REGISTER));
            }
        });

    }

    /**
     * 用户登录
     *
     * @param user
     * @return
     */
    public User login(User user) {
        //验证码校验
        String userId = user.getUserId();
        ValueCheckUtils.notEmpty(userId, "用户id不能为空");
        userSendService.validateMsgCode(userId, user.getPhoneNumber(), user.getMsgCode(), UserConst.MSG_CODE_TYPE_LOGINANDREGISTER);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(User.Phone_number + "=", user.getPhoneNumber());

        User user1 = selectOneByWhereMap(paramMap, User.class);
        Boolean ifRegister = true;
        if (user1 == null) {
            //手机号码第一次登录
            ifRegister = false;
            userService.createUser(user);
            user1 = selectOneById(user.getId(), User.class);
        }

        user1.setPassword("");
        user1.setPayPassword("");//敏感信息不返回

        user1.setAccessToken(JwtUtils.createToken(user1));
        user1.setIfRegister(ifRegister);
        return user1;
    }


    /**
     * 修改头像
     *
     * @param userId 用户id
     * @param file   头像图片
     */
    public String updateHeadPic(String userId, @RequestParam("file") MultipartFile file) {
        String imgUrl = aliOSSBusiness.uploadCommonPic(file);

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
     * 修改用户信息
     */
    public void updateInfo(String userName, Integer sex, Integer location, String birthday, String userId) {
        User user = new User();
        user.setId(userId);
        if (StringUtils.isNotBlank(userName)) {
            user.setUserName(userName);
            Map<String, Object> whereMap = ImmutableMap.of(User.User_name + "=", userName, User.Id + "!=", userId);
            User userPo = selectOneByWhereMap(whereMap, User.class);
            if (userPo != null) {
                throw new BusinessException("用户昵称已存在");
            }
        } else if (sex != null && (sex == 1 || sex == 2)) {
            user.setSex(sex);
        } else if (location != null) {
            user.setLocation(addressService.getLocation(location));
        } else if (birthday != null) {
            Timestamp timestamp;
            try {
                timestamp = new Timestamp(Long.parseLong(birthday));
            } catch (Exception e) {
                throw new BusinessException("日期格式错误");
            }
            if (timestamp.getTime() > System.currentTimeMillis()) {
                throw new BusinessException("生日不能大于当前时间");
            }
            user.setBirthday(timestamp);
        } else {
            throw new BusinessException("缺少更新数据");
        }
        if (userService.updateIfNotNull(user) == 0) {
            throw new BusinessException("修改用户信息失败");
        }
    }

    /**
     * 获取所有用户（用于展示邀请记录）
     *
     * @return
     */
    public List<User> listUser4InviteRecord(Integer memberType) {
        return userMapper.listUser4InviteRecord(memberType);
    }

    /**
     * 查询用户邀请记录
     *
     * @param userId 用户id
     * @return
     */
    public List<InviteUserRecord> listInviteUser(String userId, Integer inviteType, Pagination pagination) {
        return userMapper.listInviteUser(userId, inviteType, pagination);
    }


    /**
     * 根据短信类型获取用户id，再重置密码
     *
     * @param request     请求体
     * @param newPassword 新密码
     * @param msgType     短信类型
     */
    public void resetPassword(HttpServletRequest request, String newPassword, String phoneNumber, String msgCode, Integer msgType) throws UnsupportedEncodingException {
        String userId;
        if (UserConst.MSG_CODE_TYPE_RESET_LOGINPWD == msgType) {
            User user = userService.selectOneByWhereString(User.Phone_number + "=", phoneNumber, User.class);
            ValueCheckUtils.notEmpty(user, "该手机号未注册");
            userId = user.getUserId();
        } else if (UserConst.MSG_CODE_TYPE_RESET_PAYPWD == msgType || UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD == msgType) {
            userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        } else {
            throw new BusinessException("短信验证码类型有误");
        }

        userSendService.validateMsgCode(userId, phoneNumber, msgCode, msgType);

        resetPassword(userId, newPassword, msgType);
    }

    /**
     * 重置密码
     *
     * @param userId   用户id
     * @param password 支付密码
     * @param msgType  短信类型
     */
    public void resetPassword(String userId, String password, Integer msgType) {
        String updatePWDColumn;
        if (UserConst.MSG_CODE_TYPE_RESET_LOGINPWD == msgType) {
            updatePWDColumn = User.Password;
            ValueCheckUtils.notEmpty(password, "新密码不能为空");
        } else if (UserConst.MSG_CODE_TYPE_RESET_PAYPWD == msgType || UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD == msgType) {
            updatePWDColumn = User.Pay_password;
        } else {
            throw new BusinessException("短信类型有误");
        }

        password = EncryptUtils.md5Password(password);
        ValueCheckUtils.notEmpty(password, "新密码字符串异常");

        Integer i = userMapper.updatePassword(userId, password, updatePWDColumn);
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
    public void updatePassword(String userId, String oldPassword, String newPassword, Integer updatePwdType) {
        User userPo = selectOneById(userId, User.class);
        String phoneNumber = userPo.getPhoneNumber();

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
        newPassword = EncryptUtils.md5Password(newPassword);

        User checkUser = userMapper.getUserToUpdatePwd(phoneNumber, updatePWDColumn, oldPassword);
        ValueCheckUtils.notEmpty(checkUser, "密码有误");

        Integer i = userMapper.updatePassword(userId, newPassword, updatePWDColumn);
        if (i == 0) {
            throw new BusinessException("修改密码失败");
        }
    }

    /**
     * 附近的人
     *
     * @param userId       用户id
     * @param provinceCode 省编码
     * @param cityCode     城市编码
     * @param regionCode   区编码
     * @param streetCode   街道编码
     * @param coordinateX  经度
     * @param coordinateY  纬度
     * @param computePower 算力
     * @return
     */
    public List<NearByUser> nearUserList(String userId, String provinceCode, String cityCode, String regionCode, String streetCode, Double coordinateX, Double coordinateY, BigDecimal computePower) {
        User user = selectOneById(userId, User.class);
        ValueCheckUtils.notEmpty(user, "未找到用户");
        String headImgPic = user.getHeadPictureUrl() == null ? " " : user.getHeadPictureUrl();

        //获取用户剩余偷币用户数目
        Integer limit = (Integer) redisClient.getHashKey(userId, RedisKeyConst.buildUserStealCount(userId));

        //更新用户坐标缓存信息
        String userCoordinateKey = RedisKeyConst.buildUserCoordinate(userId, provinceCode, cityCode, regionCode, streetCode);
        String member = user.getId() + ":" + user.getUserName() + ":" + headImgPic + ":" + computePower;
        redisClient.cacheGeo(userCoordinateKey, coordinateX, coordinateY, member, 300L);

        //查找附近的人
        List<NearByUser> nearByUsers = new ArrayList<>(100);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> radiusGeo = redisClient.radiusGeo(userCoordinateKey, coordinateX, coordinateY, 1000D, Sort.Direction.DESC, Long.valueOf(limit));
        for (GeoResult<RedisGeoCommands.GeoLocation<Object>> geoResult : radiusGeo.getContent()) {
            Integer distance = Integer.parseInt(new java.text.DecimalFormat("0").format(geoResult.getDistance().getValue()));
            RedisGeoCommands.GeoLocation<Object> geoLocation = geoResult.getContent();

            //[sampleName],[userName],[headImgPic],[computePower]
            String[] nearByUserInfo = String.valueOf(geoLocation.getName()).split(":");
            NearByUser nearByUser = new NearByUser(nearByUserInfo[0], nearByUserInfo[1], nearByUserInfo[2], nearByUserInfo[3], distance);
            nearByUsers.add(nearByUser);
        }

        return nearByUsers;
    }


    /**
     * 更新用户的邀请用户id
     *
     * @param userId
     */
    public void updateInviteCode(String userId, String inviteCode) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                ValueCheckUtils.notEmpty(inviteCode, "邀请码不能为空");
                User directInviteUser = selectOneByWhereString(User.Invite_code + " = ", inviteCode, User.class); //直接邀请用户
                if (directInviteUser == null) {
                    throw new BusinessException("邀请码错误,请重新输入");
                }
                String directInviteId = directInviteUser.getId();

                User user = selectOneById(userId, User.class);
                if (StringUtils.isNotBlank(user.getInviteId())) {
                    throw new BusinessException("该用户已设置邀请人");
                }
                user.setId(userId);
                user.setInviteId(directInviteId);
                if (updateIfNotNull(user) == 0) {
                    throw new BusinessException("更新邀请用户ID失败");
                }
            }
        });
    }

    /**
     * 获取用户所有信息
     *
     * @param userId
     * @return
     */
    public User allUserInfo(String userId) {
        User user = userMapper.getUserInfo(userId);
        if (user.getCertStatus() == null) {
            user.setCertStatus(0);
        }
        return user;
    }

    /**
     * 微信用户同步
     *
     * @param wxOpenId
     * @param phoneNumber
     */
    public void synchronizingWX(String wxOpenId, String phoneNumber) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                User user = new User();
                user.setWxOpenId(wxOpenId);
                user.setPhoneNumber(phoneNumber);
                insertIfNotNull(user);
            }
        });
    }

    /**
     * 解绑 第三方
     *
     * @param type
     * @param userId
     */
    public void unbind(String type, String userId) {
        User user = getById(userId);
        int result = 0;
        switch (type) {
            case "wx":
                if (StringUtils.isBlank(user.getWxOpenId())) {
                    throw new BusinessException("您还没有绑定微信");
                }
                result = userMapper.deleteWeixin(userId);
                break;
        }
        ValueCheckUtils.notZero(result, "解绑失败");
    }


    /**
     * 更换手机号码
     */
    public void changePhone(String msgCode, String phoneNumber, String userId) {
        User user = userService.selectOneByWhereString(User.Phone_number + " = ", phoneNumber, User.class);
        if (user != null) {
            throw new BusinessException("该手机号码已经绑定其它账号");
        }
        userSendService.validateMsgCode(userId, phoneNumber, msgCode, UserConst.MSG_CODE_TYPE_CHANGE_PHONE);
        user = new User();
        user.setId(userId);
        user.setPhoneNumber(phoneNumber);
        if (userService.updateIfNotNull(user) < 1) {
            throw new BusinessException("绑定失败");
        }
    }

    public User getById(String userId) {
        return userService.selectOneById(userId, User.class);
    }

}
