package com.balance.common;

import com.balance.service.common.UserFreeCountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserFreeCountServiceTest {

    @Autowired
    private UserFreeCountService userFreeCountService;

    @Test
    public void initDayMaxCountTest() {
        userFreeCountService.initDayMaxCount();
    }

    @Test
    public void initWeekMaxCountTest() {
        userFreeCountService.initWeekMaxCount();
    }

}
