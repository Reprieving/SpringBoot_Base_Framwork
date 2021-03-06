package com.balance.mapper.sys;

import com.balance.architecture.constance.MybatisConst;
import com.balance.architecture.dto.Pagination;
import com.balance.entity.sys.Subscriber;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubscriberMapper {

    List<Subscriber> selectList(@Param(MybatisConst.PAGINATION) Pagination pagination, @Param("subscriber")Subscriber subscriber);

    Subscriber selectSubscriberByLogin(@Param("userName")String userName, @Param("password")String password);
}
