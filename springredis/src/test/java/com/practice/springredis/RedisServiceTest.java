package com.practice.springredis;

import com.practice.springredis.entity.User;
import com.practice.springredis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {
    @Autowired
    RedisService redisService;

    @Test
    public void setObject(){
        redisService.setObj("a",new User("张三",20));
    }

    @Test
    public void getObject(){
        System.out.println(redisService.getObject("a"));
    }
}
