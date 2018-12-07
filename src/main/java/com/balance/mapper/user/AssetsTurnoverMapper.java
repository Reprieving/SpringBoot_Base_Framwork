package com.balance.mapper.user;

import com.balance.entity.user.AssetsTurnover;

import java.util.List;
import java.util.Map;

public interface AssetsTurnoverMapper {

    List<AssetsTurnover> selectByPage(Map<String, Object> param);

    int selectCount(Map<String, Object> param);

}
