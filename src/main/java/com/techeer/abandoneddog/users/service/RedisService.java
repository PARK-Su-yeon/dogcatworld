package com.techeer.abandoneddog.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    // 데이터 넣기
    public void setValues(String refresh, String token){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(refresh, token, Duration.ofDays(1));
    }
    // 데이터 가져오기
    public String getValues(String refresh){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(refresh);
    }
}