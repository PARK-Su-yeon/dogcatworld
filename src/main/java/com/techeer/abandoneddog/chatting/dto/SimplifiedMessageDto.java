package com.techeer.abandoneddog.chatting.dto;

import com.techeer.abandoneddog.chatting.domain.MessageType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimplifiedMessageDto {
	private Long chatRoomId;
	private Long senderId;
	private Long receiverId;
	private String message;
	private MessageType type;
	private String chatRoomName;
}

