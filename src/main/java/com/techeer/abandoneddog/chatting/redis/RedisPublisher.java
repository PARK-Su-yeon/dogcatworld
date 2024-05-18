package com.techeer.abandoneddog.chatting.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> chatRedisTemplate;

    public void publish(ChannelTopic topic, Object message) {
        chatRedisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
