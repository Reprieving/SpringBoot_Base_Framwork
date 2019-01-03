package com.balance.service.common;


import com.balance.service.user.MiningRewardService;
import com.balance.service.user.UserMemberService;
import com.balance.service.user.UserVoucherService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ScheduledService {

    @Autowired
    private UserVoucherService userVoucherService;

    @Autowired
    private UserMemberService userMemberService;

    @Autowired
    private MiningRewardService miningRewardService;

    //更新逾期的会员记录
    @Scheduled(cron = "0 0 0 * * ?")
    public void cancelMemberRecord() {
        String expireTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        userMemberService.cancelExpireMember(expireTime);
    }

    //作废逾期的卡券
    @Scheduled(cron = "0 0 0 * * ?")
    public void cancelVoucherRecord() {
        String expireTime = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
        userVoucherService.cancelExpireVoucher(expireTime);
    }

    //挖矿收益
    @Scheduled(cron = "0 0 0 * * ?")
    public void createMining(){
        miningRewardService.createMining();
    }

}
