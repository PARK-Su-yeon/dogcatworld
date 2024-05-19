package com.techeer.abandoneddog.chatting.dto;

import com.techeer.abandoneddog.chatting.domain.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDto {
    private Long chatRoomId;
    private Long receiverId;
    private Long senderId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deleted;

    public ChatRoomResponseDto(ChatRoom chatRoom) {
        this.chatRoomId = chatRoom.getChatRoomId();
        this.receiverId = chatRoom.getReceiver().getId();
        this.senderId = chatRoom.getSender().getId();
        this.name = chatRoom.getName();
        this.createdAt = chatRoom.getCreatedAt();
        this.updatedAt = chatRoom.getUpdatedAt();
        this.deleted = chatRoom.getIsDeleted();
    }
}