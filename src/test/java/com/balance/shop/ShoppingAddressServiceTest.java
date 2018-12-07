package com.balance.shop;

import com.balance.service.shop.ShoppingAddressService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ShoppingAddressServiceTest {

    @Autowired
    private ShoppingAddressService shoppingAddressService;

    @Test
    public void getDefaultShoppingAddressTest(){
        String userId = "123";
        System.out.println(shoppingAddressService.getDefaultShoppingAddress(userId));
    }

}
