package com.techeer.abandoneddog.chatting.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.techeer.abandoneddog.chatting.dto.SimplifiedMessageDto;
import com.techeer.abandoneddog.chatting.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketMessageController {

	private final ChatService chatService;

	@MessageMapping("/message")
	public void handleMessage(SimplifiedMessageDto messageDto) {
		switch (messageDto.getType()) {
			case ENTER:
				chatService.enterChatRoom(messageDto);
				break;
			case TALK:
				chatService.sendMessageToChatRoom(messageDto);
				break;
			case LEAVE:
				chatService.leaveChatRoom(messageDto.getSenderId(), messageDto.getChatRoomId());
				break;
			default:
				log.warn("Unknown message type: {}", messageDto.getType());
		}
	}
}
