package com.balance.demo;

import com.balance.core.mybatis.mapper.BaseMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {
	}

	public static void main(String[] args) {
		System.out.println(BaseMapper.class.getSimpleName());
	}
}
