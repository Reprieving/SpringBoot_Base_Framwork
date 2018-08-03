package com.balance.sys.specs;

import com.balance.core.dto.Pagination;
import com.balance.sys.entity.Subscriber;

import java.util.List;

public interface SubscriberSpecs {
    void insertOne(Subscriber subscriber);

    List<Subscriber> selectList(Pagination pagination, Subscriber queryMap);

}
