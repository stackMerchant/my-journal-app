package com.svats.journalApp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testConnection() {
        redisTemplate.opsForValue().set("key1", "value1");
        Object value = redisTemplate.opsForValue().get("key1");
        Assertions.assertEquals("value1", value);
    }
}
