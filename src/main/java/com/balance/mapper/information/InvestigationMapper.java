package com.balance.mapper.information;


import com.balance.entity.information.Investigation;

import java.util.List;
import java.util.Map;

public interface InvestigationMapper {

    List<Investigation> selectPageWithGoods(Map<String, Object> params);

    int selectCountWithGoods(Map<String, Object> params);


    List<Investigation> selectPageWithUser(Map<String, Object> params);

    int selectCountWithUser(Map<String, Object> params);
}