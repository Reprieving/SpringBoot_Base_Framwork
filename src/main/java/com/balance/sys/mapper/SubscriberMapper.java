package com.balance.sys.mapper;

import com.balance.core.constance.MybatisConst;
import com.balance.core.dto.Pagination;
import com.balance.sys.entity.Subscriber;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubscriberMapper {
    void insertUser(Subscriber subscriber);

    List<Subscriber> selectList(@Param(MybatisConst.PAGINATION) Pagination pagination, @Param("subscriber")Subscriber subscriber);


}
