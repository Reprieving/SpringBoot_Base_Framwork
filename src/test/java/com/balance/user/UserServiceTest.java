package com.balance.user;

import com.balance.entity.user.User;
import com.balance.service.common.WjSmsService;
import com.balance.service.user.UserService;
import com.balance.utils.TreeNodeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void filterInviteUserId(){
        List<User> allUser = userService.listUser4InviteRecord();
        List<String> ids = new ArrayList<>();
        TreeNodeUtils.filterInviteUserId("f42973aba7db484a81a4f319b9e74463",allUser,ids);
        System.out.println(ids);
    }
}
