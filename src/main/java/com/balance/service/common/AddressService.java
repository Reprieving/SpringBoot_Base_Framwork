package com.balance.service.common;

import com.balance.architecture.exception.BusinessException;
import com.balance.entity.common.Address;
import com.balance.mapper.common.AddressMapper;
import org.apache.commons.collections.CollectionUtils;
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


    @Deprecated
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

    public String getLocation (Integer location) {
        List<Address> addressList = addressMapper.selectListByPid(location);
        if (CollectionUtils.isNotEmpty(addressList)) {
            throw new BusinessException("地址数据有误");
        }
        Address address;
        String str = "";
        address = addressMapper.selectById(location);
        str = address.getName() + str;
        while (address.getPid() != 0) {
            address = addressMapper.selectById(address.getPid());
            if (address == null) {
                break;
            }
            str = address.getName() + " " + str;
        }
        return str;
    }


}
