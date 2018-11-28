package com.balance.service.user;

import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.UserConst;
import com.balance.entity.common.UserFreeCount;
import com.balance.entity.user.MsgRecord;
import com.balance.mapper.user.UserFreeCountMapper;
import com.balance.mapper.user.UserMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Service
public class UserSendService extends BaseService {
    @Autowired
    private UserFreeCountMapper userFreeCountMapper;

    /**
     * 新建短信发送记录
     *
     * @param phoneNumber
     * @param msgCode
     * @param msgType
     * @throws BusinessException
     */
    public void createMsgRecord(String userId, String phoneNumber, String msgCode, Integer msgType) throws BusinessException {
        Integer result = userFreeCountMapper.updateUserSendMsgCount(userId);
        if (result == 0) {
            throw new BusinessException("当天发送短信次数已到达上限3条");
        }
        MsgRecord msgRecord = new MsgRecord(userId, phoneNumber, msgCode, msgType, new Timestamp(System.currentTimeMillis()), true);
        insertIfNotNull(msgRecord);

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
        Map<String, Object> whereMap = ImmutableMap.of(
                MsgRecord.User_id + " = ", userId, MsgRecord.Phone_number + " = ", phoneNumber,
                MsgRecord.Msg_code + " = ", msgCode, MsgRecord.Msg_type + " = ", msgType
        );
        MsgRecord msgRecord = selectOneByWhereMap(whereMap, MsgRecord.class);
        ValueCheckUtils.notEmpty(msgRecord, "短信验证码有误");

        if (!msgRecord.getIsValid()) {
            throw new BusinessException("短信验证码已经失效");
        }

        long minutes = DateUtils.getFragmentInMinutes(new Date(), Calendar.MONDAY);

        if (minutes > 5) {
            throw new BusinessException("短信验证码已经过期");
        }

    }


}
