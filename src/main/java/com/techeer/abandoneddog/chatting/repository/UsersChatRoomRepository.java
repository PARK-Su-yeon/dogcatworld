package com.techeer.abandoneddog.chatting.repository;

import com.techeer.abandoneddog.chatting.domain.ChatRoom;
import com.techeer.abandoneddog.chatting.domain.UsersChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersChatRoomRepository extends JpaRepository<UsersChatRoom, Long> {

    @Query("SELECT ucr FROM UsersChatRoom ucr WHERE ucr.user.id = :userId AND ucr.chatRoom.chatRoomId = :chatRoomId AND ucr.leftAt IS NULL")
    Optional<UsersChatRoom> findByUserIdAndChatRoomId(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);

    @Query("SELECT ucr.chatRoom FROM UsersChatRoom ucr WHERE ucr.user.id = :userId AND ucr.leftAt IS NULL")
    List<ChatRoom> findActiveChatRoomsByUserId(@Param("userId") Long userId);

}
