package com.balance.service.common;

import com.balance.architecture.service.BaseService;
import com.balance.entity.common.UserFreeCount;
import com.balance.mapper.user.UserFreeCountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class UserFreeCountService extends BaseService {

    @Autowired
    private UserFreeCountMapper userFreeCountMapper;

    /**
     * 初始化每日免费次数
     */
    public void initDayMaxCount() {
        List<String> updateField = Arrays.asList(UserFreeCount.Luck_draw_count, UserFreeCount.Send_msg_count, UserFreeCount.Share_count);
        userFreeCountMapper.initMaxCount(updateField);
    }

    /**
     * 初始化每周免费次数
     */
    public void initWeekMaxCount() {
        List<String> updateField = Arrays.asList(UserFreeCount.Beauty_receive_count);
        System.out.println(userFreeCountMapper.initMaxCount(updateField));
    }
}
