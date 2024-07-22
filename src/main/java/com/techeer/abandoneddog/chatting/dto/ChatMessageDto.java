package com.techeer.abandoneddog.chatting.dto;

import com.techeer.abandoneddog.chatting.domain.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
	private Long chatRoomId;
	private Long senderId;
	private String content;
	private MessageType type;
}
