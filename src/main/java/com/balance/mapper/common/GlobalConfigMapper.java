package com.balance.mapper.common;

import com.balance.service.common.GlobalConfigService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalConfigMapper {

    void updateValue(@Param("key") GlobalConfigService.Enum key, @Param("value") Integer value);
}
