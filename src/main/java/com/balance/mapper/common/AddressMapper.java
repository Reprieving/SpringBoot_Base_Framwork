package com.balance.mapper.common;


import com.balance.entity.common.Address;

import java.util.List;

public interface AddressMapper {

    List<Address> selectListByPid(int pid);

    Address selectById(int id);
}
