package com.balance.service.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.balance.architecture.exception.BusinessException;
import com.balance.architecture.service.BaseService;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.constance.SettlementConst;
import com.balance.constance.UserConst;
import com.balance.entity.user.User;
import com.balance.entity.user.UserAssets;
import com.balance.entity.user.UserFrozenAssets;
import com.balance.mapper.user.UserAssetsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserAssetsService extends BaseService {
    @Autowired
    private UserAssetsMapper userAssetsMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private RedisClient redisClient;

    public String getSettlementNameById(Integer settlementId) {
        switch (settlementId) {
            case SettlementConst.SETTLEMENT_IH:
                return "PT";

            case SettlementConst.SETTLEMENT_ETH:
                return "ETH";

            case SettlementConst.SETTLEMENT_ORE:
                return "矿石";

            case SettlementConst.SETTLEMENT_COMPUTING_POWER:
                return "颜值";

            case SettlementConst.SETTLEMENT_CANDY:
                return "糖果";
        }
        return null;
    }


    /**
     * 获取用户的资产
     *
     * @param userId
     * @return
     */
    public UserAssets getAssetsByUserId(String userId) {
        return selectOneByWhereString(UserAssets.User_id + " = ", userId, UserAssets.class);
    }

    /**
     * 通过支付方式获取用户的资产
     *
     * @param userAssets   用户资产
     * @param settlementId 支付方式
     * @return
     * @throws BusinessException
     */
    public BigDecimal getAssetsBySettlementId(UserAssets userAssets, Integer settlementId) throws BusinessException {
        switch (settlementId) {
            case SettlementConst.SETTLEMENT_IH:
                return userAssets.getIh();

            case SettlementConst.SETTLEMENT_ETH:
                return userAssets.getEth();

            case SettlementConst.SETTLEMENT_ORE:
                return userAssets.getOre();

            case SettlementConst.SETTLEMENT_COMPUTING_POWER:
                return userAssets.getComputePower();
        }
        throw new BusinessException("错误的支付方式");
    }

    /**
     * 更改用户资产
     *
     * @param userId       用户id
     * @param amount       数目（正数为加，负数为减）
     * @param settlementId 支付方式
     * @return
     */
    public Integer changeUserAssets(String userId, BigDecimal amount, Integer settlementId) {
        UserAssets userAssets = selectOneByWhereString(UserAssets.User_id + " = ", userId, UserAssets.class);
        return changeUserAssets(userId, amount, settlementId, userAssets);
    }

    /**
     * 更改用户资产
     *
     * @param userId       用户id
     * @param amount       数目（正数为加，负数为减）
     * @param settlementId 支付方式
     * @param userAssets   用户资产实体
     * @return
     */
    public Integer changeUserAssets(String userId, BigDecimal amount, Integer settlementId, UserAssets userAssets) {
        String assetColumnName = null;
        switch (settlementId) {
            case SettlementConst.SETTLEMENT_IH:
                assetColumnName = UserAssets.Id;
                break;

            case SettlementConst.SETTLEMENT_ETH:
                assetColumnName = UserAssets.Eth;
                break;

            case SettlementConst.SETTLEMENT_ORE:
                assetColumnName = UserAssets.Ore;
                break;

            case SettlementConst.SETTLEMENT_COMPUTING_POWER:
                assetColumnName = UserAssets.Compute_power;
                break;

            case SettlementConst.SETTLEMENT_RMB:
                assetColumnName = UserAssets.RMB;
                break;
        }

        return userAssetsMapper.updateUserAssets(userId, amount, assetColumnName, userAssets.getVersion());
    }


    /**
     * 更改用户冻结资产
     *
     * @param userId       用户id
     * @param amount       数目（正数为加，负数为减）
     * @param settlementId 支付方式
     * @return
     */
    public Integer changeUserFrozenAssets(String userId, BigDecimal amount, Integer settlementId) {
        UserFrozenAssets userFrozenAssets = selectOneByWhereString(UserFrozenAssets.User_id + " = ", userId, UserFrozenAssets.class);
        return changeUserFrozenAssets(userId, amount, settlementId, userFrozenAssets);
    }

    /**
     * 更改用户资产
     *
     * @param userId           用户id
     * @param amount           数目（正数为加，负数为减）
     * @param settlementId     支付方式
     * @param userFrozenAssets 用户冻结资产实体
     * @return
     */
    public Integer changeUserFrozenAssets(String userId, BigDecimal amount, Integer settlementId, UserFrozenAssets userFrozenAssets) {
        String assetColumnName = null;
        switch (settlementId) {
            case SettlementConst.SETTLEMENT_IH:
                assetColumnName = "ih";
                break;

            case SettlementConst.SETTLEMENT_ETH:
                assetColumnName = "eth";
                break;

            case SettlementConst.SETTLEMENT_ORE:
                assetColumnName = "ore";
                break;

            case SettlementConst.SETTLEMENT_COMPUTING_POWER:
                assetColumnName = "compute_power";
                break;
        }
        return userAssetsMapper.updateUserFrozenAssets(userId, amount, assetColumnName, userFrozenAssets.getVersion());
    }

    /**
     * 更新算例排行榜(颜值排行榜)
     *
     * @return
     */
    public void updateComputePowerRank() {
        Integer computePowerRankLength = UserConst.COMPUTE_POWER_RANK_LENGTH;
        List<Object> topRankUser = new ArrayList<>(UserConst.COMPUTE_POWER_RANK_LENGTH);
        List<UserAssets> userComputePowerList = userAssetsMapper.listComputePower(UserConst.USER_STATUS_NORMAL);
        int i = 1;
        for (UserAssets userAssets : userComputePowerList) {
            if (topRankUser.size() < computePowerRankLength) {
                topRankUser.add(userAssets);
            }
            redisClient.set(RedisKeyConst.COMPUTE_POWER_RANK_NO + userAssets.getUserId(), String.valueOf(i)); //用户算力名次
            i++;
        }
        redisClient.listTrim(RedisKeyConst.COMPUTE_POWER_RANK_LIST, computePowerRankLength, computePowerRankLength + 1);
        redisClient.leftPushAll(RedisKeyConst.COMPUTE_POWER_RANK_LIST, topRankUser);
    }
}
