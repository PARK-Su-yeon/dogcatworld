package com.techeer.abandoneddog.chatting.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.techeer.abandoneddog.global.entity.BaseEntity;
import com.techeer.abandoneddog.users.entity.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE chat_room SET deleted = true WHERE chat_room_id = ?")
@Where(clause = "deleted = false")
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id", updatable = false)
	private Long chatRoomId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private Users receiver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private Users sender;

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Message> messages = new ArrayList<>();

	@OneToMany(mappedBy = "chatRoom")
	private List<UsersChatRoom> usersChatRooms = new ArrayList<>();

	@Column(name = "name")
	private String name;

	@Builder
	public ChatRoom(Long chatRoomId, Users receiver, Users sender, String name) {
		this.chatRoomId = chatRoomId;
		this.receiver = receiver;
		this.sender = sender;
		this.name = name;
	}
}