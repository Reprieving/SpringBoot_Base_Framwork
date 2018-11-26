package com.balance.service.user;

import com.balance.architecture.service.BaseService;
import com.balance.entity.user.MiningRuler;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MiningRulerService extends BaseService {

    /**
     * 所有挖矿规则
     *
     * @return
     */
    public List<MiningRuler> listMiningRuler() {
        return selectAll(null, MiningRuler.class);
    }

    /**
     * 根据算力获取对应的挖矿比例
     *
     * @param computePower
     * @return
     */
    public BigDecimal getMiningRateByComputePower(BigDecimal computePower) {
        List<MiningRuler> miningRulerList = listMiningRuler();
        for (MiningRuler miningRuler : miningRulerList) {
            Double computePowerD = computePower.doubleValue();
            if (computePowerD >= miningRuler.getStartValue().doubleValue() && computePowerD < miningRuler.getEndValue().doubleValue()) {
                return miningRuler.getRewardRate();
            }
        }
        return null;
    }
}
