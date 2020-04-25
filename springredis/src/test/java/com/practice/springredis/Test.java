package com.practice.springredis;

import com.practice.springredis.service.StringRedisService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {
    @Autowired
    StringRedisService stringRedisService;

    @org.junit.Test
    public void set(){
        stringRedisService.setString("name","张三");
    }

    @org.junit.Test
    public void get(){
        System.out.println(stringRedisService.getString("name"));
    }
}
