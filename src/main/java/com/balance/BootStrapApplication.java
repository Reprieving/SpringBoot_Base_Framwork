package com.balance;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.balance.*.mapper")
@EnableCaching
public class BootStrapApplication {
	public static void main(String[] args) {
		SpringApplication.run(BootStrapApplication.class, args);
	}
}
