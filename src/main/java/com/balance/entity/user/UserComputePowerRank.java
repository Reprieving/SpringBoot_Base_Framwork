package com.balance.entity.user;

import lombok.Data;

import java.util.List;

@Data
public class UserComputePowerRank {
    private Integer computeRankNo;
    private List<UserAssets> userAssetsList;
}
