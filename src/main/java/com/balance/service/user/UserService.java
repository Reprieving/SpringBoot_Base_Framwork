package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.client.RedisClient;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.MissionConst;
import com.balance.constance.RedisKeyConst;
import com.balance.constance.UserConst;
import com.balance.entity.common.UserFreeCount;
import com.balance.entity.common.UserInviteCodeId;
import com.balance.entity.mission.Mission;
import com.balance.entity.user.*;
import com.balance.mapper.common.AutoIncreaseIdMapper;
import com.balance.mapper.user.MiningRewardMapper;
import com.balance.mapper.user.UserMapper;
import com.balance.service.common.AliOSSBusiness;
import com.balance.service.mission.MissionCompleteService;
import com.balance.service.mission.MissionService;
import com.balance.utils.BigDecimalUtils;
import com.balance.utils.EncryptUtils;
import com.balance.utils.RandomUtil;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

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
    private MissionService missionService;

    @Autowired
    private MissionCompleteService missionCompleteService;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private UserSendService userSendService;

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
                    throw new BusinessException("手机号已被注册");
                }


                user.setUserName("");
                user.setCreateTime(new Timestamp(System.currentTimeMillis()));

                //生成邀请码
                UserInviteCodeId userInviteCodeId = new UserInviteCodeId();
                autoIncreaseIdMapper.insertUserInviteCode(userInviteCodeId);
                String newInviteCode = RandomUtil.randomInviteCode(userInviteCodeId.getId());

                user.setInviteCode(newInviteCode);
                user.setPassword(EncryptUtils.md5Password(user.getPassword()));

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

                //完成新用户注册任务
                Mission mission = missionService.filterTaskByCode(MissionConst.NEW_REGISTER, missionService.selectAll(null, Mission.class));
                missionCompleteService.createOrUpdateMissionComplete(userId, mission);

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
        //验证码校验
        userSendService.validateMsgCode(user.getId(), user.getPhoneNumber(), user.getMsgCode(), UserConst.MSG_CODE_TYPE_LOGINANDREGISTER);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(User.Phone_number + "=", user.getPhoneNumber());

        User user1 = selectOneByWhereMap(paramMap, User.class);
        User rspData = new User();
        if (user1 == null) {
            rspData.setIsRegister(false);
            userService.createUser(user);
            user1 = selectOneById(user.getId(),User.class);
        }else {
            rspData.setIsRegister(true);
        }
        rspData.setUserName(user1.getUserName());
        rspData.setHeadPictureUrl(user1.getHeadPictureUrl());
        rspData.setAccessToken(JwtUtils.createToken(user1));

        return rspData;
    }


    /**
     * 修改头像
     *
     * @param userId 用户id
     * @param file   头像图片
     */
    public String updateHeadPic(String userId, @RequestParam("file")MultipartFile file) {
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
        Map<String, Object> whereMap = ImmutableMap.of(User.User_name + "=", userName, User.Id + "!=", userId);
        User userPo = selectOneByWhereMap(whereMap, User.class);
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
     *
     * @return
     */
    public List<User> listUser4InviteRecord() {
        return userMapper.listUser4InviteRecord();
    }

    /**
     * 查询用户邀请记录
     *
     * @param userId 用户id
     * @return
     */
    @Cacheable(value = "listInviteUser", sync = true)
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
        String updatePWDColumn;
        if (UserConst.MSG_CODE_TYPE_RESET_LOGINPWD == msgType) {
            updatePWDColumn = User.Password;
            ValueCheckUtils.notEmpty(newPassword, "新密码不能为空");
        } else if (UserConst.MSG_CODE_TYPE_RESET_PAYPWD == msgType || UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD == msgType) {
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

            //[userId],[userName],[headImgPic],[computePower],[distance]
            String[] nearByUserInfo = String.valueOf(geoLocation.getName()).split(":");
            NearByUser nearByUser = new NearByUser(nearByUserInfo[0], nearByUserInfo[1], nearByUserInfo[2], nearByUserInfo[3], distance);
            nearByUsers.add(nearByUser);
        }

        return nearByUsers;
    }


    /**
     * 更新用户的邀请用户id
     * @param userId
     */
    public void updateInviteCode(String userId,String inviteCode) {
        ValueCheckUtils.notEmpty(inviteCode,"邀请码不能为空");
        User inviteUser = selectOneByWhereString(User.Invite_code + " = ", inviteCode, User.class);
        String inviteId = inviteUser == null ? "" : inviteUser.getId();
        User user = new User();
        user.setId(userId);
        user.setInviteId(inviteId);
        if(updateIfNotNull(user)==0){
            throw new BusinessException("更新邀请用户ID失败");
        }
    }
}
