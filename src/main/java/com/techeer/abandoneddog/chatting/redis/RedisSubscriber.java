package com.techeer.abandoneddog.chatting.redis;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techeer.abandoneddog.chatting.dto.MessageResponseDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {
	private final SimpMessageSendingOperations messagingTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String msg = new String(message.getBody(), StandardCharsets.UTF_8);
			MessageResponseDto messageResponseDto = objectMapper.readValue(msg, MessageResponseDto.class);
			messagingTemplate.convertAndSend("/topic/chat/room/" + messageResponseDto.getChatRoomId(),
				messageResponseDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
