package com.techeer.abandoneddog.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service

public class RedisServiceImpl implements RedisService {


    private final RedisTemplate<String, Object> redisTemplate;

    public RedisServiceImpl(@Qualifier("UserRedisTemplate") RedisTemplate<String, Object> userRedisTemplate) {
        this.redisTemplate = userRedisTemplate;
    }

    public void setValues(String username, String token) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(username, token, Duration.ofDays(1));
    }

    public String getValues(String refresh) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        return (String) values.get(refresh);
    }

    public void deleteValues(String refresh) {
        redisTemplate.delete(refresh);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}