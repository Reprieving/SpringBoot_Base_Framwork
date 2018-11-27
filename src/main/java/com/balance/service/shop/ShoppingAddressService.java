package com.balance.service.shop;

import com.balance.architecture.service.BaseService;
import com.balance.entity.shop.ShoppingAddress;
import com.balance.mapper.shop.ShoppingAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingAddressService extends BaseService{

    @Autowired
    private ShoppingAddressMapper shoppingAddressMapper;

    public void updateDefaultAddress(String addressId){
        ShoppingAddress shoppingAddress = new ShoppingAddress(addressId,true);
        updateIfNotNull(shoppingAddress);

        shoppingAddressMapper.updateDefault(addressId);
    }
}
