package com.practice.springredis.service;

import com.practice.springredis.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void setObj(String key, User user){
        redisTemplate.opsForValue().set(key,user);
    }

    public User getObject(String key){
        return (User) redisTemplate.opsForValue().get(key);
    }


}
