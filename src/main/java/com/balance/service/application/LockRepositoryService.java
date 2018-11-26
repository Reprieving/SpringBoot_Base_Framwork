package com.balance.service.application;

import com.balance.architecture.dto.Pagination;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.architecture.utils.ValueCheckUtils;
import com.balance.constance.ApplicationConst;
import com.balance.constance.AssetTurnoverConst;
import com.balance.constance.SettlementConst;
import com.balance.entity.application.LockRepository;
import com.balance.entity.application.LockRepositoryOrder;
import com.balance.entity.user.UserAssets;
import com.balance.service.user.AssetsTurnoverService;
import com.balance.service.user.UserAssetsService;
import com.balance.utils.BigDecimalUtils;
import com.balance.utils.MineDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LockRepositoryService extends BaseService {

    @Autowired
    private UserAssetsService userAssetsService;

    @Autowired
    private AssetsTurnoverService assetsTurnoverService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 查询锁仓产品列表
     *
     * @param lockRepository
     * @return
     */
    public List<LockRepository> listLockRepository(LockRepository lockRepository, Pagination pagination) {
        Map<String, Object> whereMap = new HashMap<>();
        if (lockRepository.getPeriod() != null) {
            whereMap.put("period = ", lockRepository.getPeriod());
        }
        if (lockRepository.getLockType() != null) {
            whereMap.put("lock_type = ", lockRepository.getLockType());
        }
        if (lockRepository.getStatus() != null) {
            whereMap.put("status = ", lockRepository.getStatus());
        }
        if (lockRepository.getStartTime() != null) {
            whereMap.put("create_time >= ", lockRepository.getCreateTime());
        }

        if (lockRepository.getEndTime() != null) {
            whereMap.put("end_time <= ", lockRepository.getEndTime());
        }

        return selectListByWhereMap(whereMap, pagination, LockRepository.class);

    }

    /**
     * 购买锁仓
     *
     * @param userId
     */
    public void createOrder(String userId, String lockRepositoryId, BigDecimal buyAmount) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                LockRepository lockRepository = selectOneById(lockRepositoryId, LockRepository.class);
                ValueCheckUtils.notEmpty(lockRepository, "未找到锁仓产品");

                //锁仓剩余额度
                int a = lockRepository.getSurplusAmount().compareTo(buyAmount);
                if (a == -1) {
                    throw new BusinessException("购买额度不能大于剩余额度");
                }

                //1.扣除用户资产
                UserAssets userAssets = selectOneByWhereString("user_id = ", userId, UserAssets.class);
                BigDecimal userIh = userAssetsService.getAssetsBySettlementId(userAssets, SettlementConst.SETTLEMENT_IH);
                int b = userIh.compareTo(buyAmount);
                if (b == -1) {
                    throw new BusinessException("用户美钻不足");
                }
                userAssetsService.changeUserAssets(userId, BigDecimalUtils.transfer2Negative(buyAmount), SettlementConst.SETTLEMENT_IH, userAssets);

                //2.计算总收益,增加锁仓产品订单记录
                Integer days = getDayFromLockType(lockRepository.getLockType());
                Timestamp expireTime = MineDateUtils.plusDay((days), new Date());

                BigDecimal totalIncome = BigDecimalUtils.multiply(BigDecimalUtils.multiply(buyAmount, lockRepository.getDailyRate()), new BigDecimal(days));

                LockRepositoryOrder order = new LockRepositoryOrder(lockRepository.getId(), userId, buyAmount, totalIncome, ApplicationConst.LOCKREPOSITORY_ORDER_STATUS_NONE, expireTime);

                insertIfNotNull(order);

                //3.增加流水记录
                assetsTurnoverService.createAssetsTurnover(userId, AssetTurnoverConst.TURNOVER_TYPE_APPLET_REWARD, BigDecimalUtils.transfer2Negative(buyAmount),
                        userId, AssetTurnoverConst.COMPANY_ID, userAssets, SettlementConst.SETTLEMENT_IH, "购买锁仓产品支出");


                //4.更改锁仓产品已投额度和剩余额度
                LockRepository lockRepository4Update = new LockRepository();
                lockRepository4Update.setId(lockRepositoryId);
                lockRepository4Update.setPurchasedAmount(BigDecimalUtils.add(buyAmount, lockRepository.getPurchasedAmount()));
                lockRepository4Update.setSurplusAmount(BigDecimalUtils.subtract(lockRepository.getSurplusAmount(), buyAmount));

                updateIfNotNull(lockRepository4Update);


            }
        });
    }

    /**
     * 开放锁仓产品
     */
    public void updateLockRepToOpening() {

    }

    /**
     * 关闭锁仓产品
     */
    public void updateLockRepToClose() {

    }

    /**
     * 计算锁仓订单收益
     */
    public void updateLockRepOrderToReward(){

    }


    /**
     * 从锁仓产品类型获取天数
     *
     * @param lockType
     * @return
     */
    public Integer getDayFromLockType(Integer lockType) {
        switch (lockType) {
            case ApplicationConst.LOCKREPOSITORY_TYPE_MONTH:
                return 30;

            case ApplicationConst.LOCKREPOSITORY_TYPE_SEASON:
                return 90;

            case ApplicationConst.LOCKREPOSITORY_TYPE_HALF_YEAR:
                return 180;

            case ApplicationConst.LOCKREPOSITORY_TYPE_WHILE_YEAR:
                return 365;
        }
        return null;
    }


}
