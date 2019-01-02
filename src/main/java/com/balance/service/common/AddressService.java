package com.balance.service.common;

import com.balance.architecture.exception.BusinessException;
import com.balance.client.RedisClient;
import com.balance.constance.RedisKeyConst;
import com.balance.entity.common.Address;
import com.balance.mapper.common.AddressMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 国家省份城市地区
 */
@CacheConfig(cacheNames = "addressCache")
@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private RedisClient redisClient;

    /**
     * 通过 父 ID 获取列表
     */
    public List<Address> getByPid(String pid) {
        Object hashList = redisClient.getHashKey(RedisKeyConst.ADDRESS_LIST, pid);
        if (hashList != null) {
            return (List<Address>) hashList;
        }
        return Collections.emptyList();
    }

    /**
     * 通过最后一级ID 得到 国家省市区
     */
    public String getLocation(Integer location) {
        List<Address> addressList = getByPid(String.valueOf(location));
        if (CollectionUtils.isNotEmpty(addressList)) {
            throw new BusinessException("地址数据有误");
        }
        Address address;
        String str = "";
        address = getById(location);
        str = address.getName() + str;
        while (address.getPid() != 0) {
            address = getById(address.getPid());
            if (address == null) {
                break;
            }
            str = address.getName() + " " + str;
        }
        return str;
    }

    /**
     * 通过 主键ID 查询, 第一次查询进行缓存
     */
    @Cacheable(key = "#id")
    public Address getById(Integer id) {
        return addressMapper.selectById(id);
    }

    /**
     * 清除所有缓存
     */
    @CacheEvict(allEntries = true)
    public void evict() {
    }

}
