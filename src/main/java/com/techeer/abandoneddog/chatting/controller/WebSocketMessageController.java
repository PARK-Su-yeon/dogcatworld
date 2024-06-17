package com.techeer.abandoneddog.chatting.controller;

import com.techeer.abandoneddog.chatting.dto.SimplifiedMessageDto;
import com.techeer.abandoneddog.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketMessageController {

    private final ChatService chatService;

    @MessageMapping("/message/enter")
    public void handleEnterMessage(SimplifiedMessageDto messageDto) {
        chatService.enterChatRoom(messageDto);
    }

    @MessageMapping("/message/talk")
    public void handleTalkMessage(SimplifiedMessageDto messageDto) {
        chatService.sendMessageToChatRoom(messageDto);
    }

    @MessageMapping("/message/leave")
    public void handleLeaveMessage(SimplifiedMessageDto messageDto) {
        chatService.leaveChatRoom(messageDto.getSenderId(), messageDto.getChatRoomId());
    }
}
