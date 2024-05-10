package com.techeer.abandoneddog.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate redisTemplate;

    public void setValues(String username, String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(username, token, Duration.ofDays(1));
    }

    public String getValues(String refresh) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(refresh);
    }

    public void deleteValues(String refresh) {
        redisTemplate.delete(refresh);
    }
}