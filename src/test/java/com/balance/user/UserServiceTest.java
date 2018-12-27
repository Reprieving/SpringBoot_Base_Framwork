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
    public void nearUserListTest() {
        System.out.println(1);
    }

    @Test
    public void sendMsgTest() {
        String msgCode = "123";
        String msgTypeStr = "【银行卡提现】";

        String content = "：【" + msgCode + "】 正在尝试" + msgTypeStr + "。 验证码十五分钟内有效 ";
        wjSmsService.sendSms("13432280678", "46792", content, "86");
    }

    @Test
    public void filterUserDownTreeNode() {
        List<User> allUser = userService.listUser4InviteRecord(0);
        List<User> UserDownTreeNodeList = new ArrayList<>();
        TreeNodeUtils.filterUserDownTreeNode("05c55b1df93011e8854100163e0c24cd", allUser, UserDownTreeNodeList);
        System.out.println(UserDownTreeNodeList);
    }
}
