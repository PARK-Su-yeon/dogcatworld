package com.techeer.abandoneddog.chatting.dto;

import com.techeer.abandoneddog.chatting.domain.MessageType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
	private Long chatRoomId;
	private Long messageId;
	private Long senderId;
	private String message;
	private MessageType type;
	private String senderUsername;
	//    private LocalDateTime createdAt;
}
