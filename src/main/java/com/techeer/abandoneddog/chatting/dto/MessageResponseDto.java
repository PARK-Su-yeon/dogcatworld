package com.techeer.abandoneddog.chatting.dto;

import com.techeer.abandoneddog.chatting.domain.MessageType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
    private Long messageId;
    private Long senderId;
    private String message;
    private MessageType type;
    private String senderUsername;
    private LocalDateTime createdAt;
}
