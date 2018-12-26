package com.balance.mapper.information;


import com.balance.architecture.dto.Pagination;
import com.balance.entity.information.Investigation;

import java.util.List;

public interface InvestigationMapper {

    List<Investigation> selectPageWithGoods(Pagination<Investigation> pagination);

    List<Investigation> selectPageWithUser(Pagination<Investigation> pagination);

}