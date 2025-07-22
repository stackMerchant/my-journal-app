package com.svats.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svats.journalApp.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    ObjectMapper mapper = new ObjectMapper();

    public <V> V get(String key, Class<V> valueClass) {
        return ExceptionUtils.handleException(() -> {
            Object valueObject = redisTemplate.opsForValue().get(key);
            return mapper.readValue(valueObject.toString(), valueClass);
        }, null);
    }

    public <V> void set(String key, V value, Long ttlSeconds) {
        ExceptionUtils.handleException(() -> {
            String valueString = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, valueString, Duration.ofSeconds(ttlSeconds));
        });
    }

    public void delete(String key) {
        ExceptionUtils.handleException(() -> {
            redisTemplate.opsForValue().getAndDelete(key);
        });
    }

}
