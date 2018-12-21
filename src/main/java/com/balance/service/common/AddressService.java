package com.balance.service.common;

import com.balance.architecture.exception.BusinessException;
import com.balance.entity.common.Address;
import com.balance.mapper.common.AddressMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    public List<Address> getByPid (int pid) {
        return addressMapper.selectListByPid(pid);
    }


    public String getLocation (String location) {
        String[] split = location.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String id = split[i];
            Address address = addressMapper.selectById(Integer.parseInt(id));
            if (address.getLevel() != i) {
                throw new BusinessException("地址数据有误");
            }
            sb.append(address.getName()).append(StringUtils.SPACE);
        }
        return sb.toString().substring(0, sb.lastIndexOf(StringUtils.SPACE));
    }
}
