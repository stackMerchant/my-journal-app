package com.svats.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        return handleException(() -> {
            Object valueObject = redisTemplate.opsForValue().get(key);
            return mapper.readValue(valueObject.toString(), valueClass);
        });
    }

    public <V> void set(String key, V value, Long ttlSeconds) {
        handleException(() -> {
            String valueString = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, valueString, Duration.ofSeconds(ttlSeconds));
            return 0;
        });
    }

    public void delete(String key) {
        handleException(() -> redisTemplate.opsForValue().getAndDelete(key));
    }

    @FunctionalInterface
    private interface CheckedSupplier<T> {
        T get() throws Exception;
    }

    private static <T> T handleException(CheckedSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.info("Exception occurred: {}", e.getMessage());
            return null;
        }
    }

}
