package com.balance.service.user;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.JwtUtils;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.UserConst;
import com.balance.entity.common.UserFreeCount;
import com.balance.entity.user.MsgRecord;
import com.balance.entity.user.User;
import com.balance.mapper.user.UserFreeCountMapper;
import com.balance.mapper.user.UserMapper;
import com.balance.service.common.WjSmsService;
import com.balance.utils.RandomUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserSendService extends BaseService {
    @Autowired
    private UserService userService;

    @Autowired
    private WjSmsService wjSmsService;

    @Autowired
    private UserFreeCountMapper userFreeCountMapper;

    /**
     * 新建短信发送记录
     * @throws BusinessException
     */
    public String createMsgRecord(HttpServletRequest request,User userReq) throws BusinessException, UnsupportedEncodingException {
        String msgCode = RandomUtil.randomNumber(6);
        String userId;
        Integer msgType = userReq.getMsgType();
        String phoneNumber = userReq.getPhoneNumber();
        if (UserConst.MSG_CODE_TYPE_RESET_LOGINPWD == msgType) { //重置登陆密码
            User user = userService.selectOneByWhereString(User.Phone_number + "=", phoneNumber, User.class);
            ValueCheckUtils.notEmpty(user, "该手机号未注册");
            userId = user.getUserId();
        } else if (UserConst.MSG_CODE_TYPE_RESET_PAYPWD == msgType || UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD == msgType) { //重置,设置支付密码
            userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
        } else {
            userId = userReq.getUserId();
        }

        ValueCheckUtils.notEmpty(userId,"用户id不能为空");

        User user;
        String msgTypeStr = "";
        switch (msgType) {
            case UserConst.MSG_CODE_TYPE_LOGINANDREGISTER:
                msgTypeStr = "[注册/登陆]";
                break;
            case UserConst.MSG_CODE_TYPE_RESET_LOGINPWD:
                msgTypeStr = "[重置登录密码]";
                break;
            case UserConst.MSG_CODE_TYPE_SETTLE_PAYPWD:
                msgTypeStr = "[设置支付密码]";
                break;
            case UserConst.MSG_CODE_TYPE_RESET_PAYPWD:
                msgTypeStr = "[重置支付密码]";
                break;
            case UserConst.MSG_CODE_TYPE_UNBIND_PHONE:
                userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
                user = userService.selectOneById(userId, User.class);
                if (user == null || !phoneNumber.equals(user.getPhoneNumber())) {
                    throw new BusinessException("解绑手机号码和已绑定手机号码不一致");
                }
                msgTypeStr = "[解绑手机号码]";
                break;
            case UserConst.MSG_CODE_TYPE_BIND_PHONE:
                userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
                user = userService.selectOneByWhereString(User.Phone_number + " = ", phoneNumber, User.class);
                if (user != null) {
                    throw new BusinessException("该手机号码已经绑定其它账号");
                }
                msgTypeStr = "[绑定手机号码]";
                break;
            case UserConst.MSG_CODE_TYPE_BIND_BANK:
                userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
                phoneNumber = selectOneById(userId, User.class).getPhoneNumber();
                msgTypeStr = "[绑定银行卡]";
                break;
            case UserConst.MSG_CODE_TYPE_BANK_WITHDRAW:
                userId = JwtUtils.getUserByToken(request.getHeader(JwtUtils.ACCESS_TOKEN_NAME)).getId();
                phoneNumber = selectOneById(userId, User.class).getPhoneNumber();
                msgTypeStr = "[银行卡提现]";
                break;
        }

        //验证发送短信次数
        List<MsgRecord> msgRecords = selectListByWhereString(MsgRecord.User_id + "=", userId, new Pagination(), MsgRecord.class);
//        if (msgRecords.size() > 0) {
//            Integer result = userFreeCountMapper.updateUserSendMsgCount(userId);
//            if (result == 0) {
//                throw new BusinessException("当天发送短信次数已到达上限3条");
//            }
//        }
        String content = "美妆连" + msgTypeStr + "验证码： " + msgCode + "，十五分钟内有效。【美妆连】";
        wjSmsService.sendSms(userReq.getPhoneNumber(), content);
        MsgRecord msgRecord = new MsgRecord(userId, phoneNumber, msgCode, msgType, new Timestamp(System.currentTimeMillis()), true);
        if(insertIfNotNull(msgRecord)==0){
            throw new BusinessException("发送短信验证码失败");
        }

        return msgCode;
    }

    /**
     * 验证短信验证码
     *
     * @param userId
     * @param phoneNumber
     * @param msgCode
     * @return
     */
    public void validateMsgCode(String userId, String phoneNumber, String msgCode, Integer msgType) {
        ValueCheckUtils.notEmpty(phoneNumber, "手机号不能为空");
        ValueCheckUtils.notEmpty(msgCode, "短信验证码不能为空");
        ValueCheckUtils.notEmpty(msgType, "短信类型不能为空");

        Map<String, Object> whereMap = ImmutableMap.of(
                MsgRecord.User_id + " = ", userId, MsgRecord.Phone_number + " = ", phoneNumber,
                MsgRecord.Msg_code + " = ", msgCode, MsgRecord.Msg_type + " = ", msgType
        );
        MsgRecord msgRecord = selectOneByWhereMap(whereMap, MsgRecord.class);
        ValueCheckUtils.notEmpty(msgRecord, "短信验证码有误");

        if (!msgRecord.getIfValid()) {
            throw new BusinessException("短信验证码已经失效");
        }

        long minutes = DateUtils.getFragmentInMinutes(new Date(), Calendar.MINUTE);

        if (minutes > 15) {
            throw new BusinessException("短信验证码已经过期");
        }

    }

    public void validateMsgCode(String userId, String msgCode, Integer msgType) {
        validateMsgCode(userId, userService.selectOneById(userId, User.class).getPhoneNumber(), msgCode, msgType);
    }


}
