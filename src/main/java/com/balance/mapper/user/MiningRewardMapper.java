package com.balance.mapper.user;

import com.balance.entity.user.MiningReward;
import org.springframework.stereotype.Repository;

@Repository
public interface MiningRewardMapper {

    /**
     * 增加收益被偷次数
     * @param miningReward
     * @return
     */
    Integer updateStolenCount(MiningReward miningReward);
}
