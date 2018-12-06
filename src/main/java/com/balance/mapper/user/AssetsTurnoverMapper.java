package com.balance.mapper.user;

import com.balance.architecture.dto.Pagination;
import com.balance.entity.user.AssetsTurnover;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AssetsTurnoverMapper {

    List<AssetsTurnover> selectByPage(@Param("page") Pagination pagination,
                                      @Param("userName")String userName, @Param("phoneNumber")String phoneNumber);

    int selectCount(@Param("userName")String userName, @Param("phoneNumber")String phoneNumbe);

}
