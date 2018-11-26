package com.balance.service.common;

import com.balance.entity.common.UserInviteCodeId;
import com.balance.mapper.common.AutoIncreaseIdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoIncreaseIdService {

    @Autowired
    private AutoIncreaseIdMapper autoIncreaseIdMapper;

    private Integer createUserInviteCodeId(UserInviteCodeId userInviteCodeId){
        return autoIncreaseIdMapper.insertUserInviteCode(userInviteCodeId);
    }
}
