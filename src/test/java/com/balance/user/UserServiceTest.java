package com.balance.user;

import com.balance.service.common.WjSmsService;
import com.balance.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private WjSmsService wjSmsService;

    @Test
    public void nearUserListTest(){
        System.out.println(1);
    }

    @Test
    public void sendMsgTest(){
        wjSmsService.sendSms("13432280678","【签名内容签名内容签名内容】");
    }
}
