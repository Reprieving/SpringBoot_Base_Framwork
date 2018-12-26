package com.balance.shop;

import com.balance.service.shop.SampleMachineService;
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


    @Autowired
    private SampleMachineService sampleMachineService;

    @Test
    public void getDefaultShoppingAddressTest(){
        String userId = "123";
        System.out.println(shoppingAddressService.getDefaultShoppingAddress(userId));
    }

    @Test
    public void updateSampleMachineLocationTest(){
        sampleMachineService.updateSampleMachineLocation("");
    }


    @Test
    public void listSampleMachineLocationTest(){
        System.out.println(sampleMachineService.listSampleMachineLocation("440300",113.981165,22.5676));
    }

}
