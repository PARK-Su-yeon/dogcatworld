package com.techeer.abandoneddog.chatting.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.abandoneddog.chatting.dto.ChatMessageDto;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            ChatMessageDto chatMessageDto = objectMapper.readValue(msg, ChatMessageDto.class);
            messagingTemplate.convertAndSend("/topic/chat/room/" + chatMessageDto.getChatRoomId(), chatMessageDto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
