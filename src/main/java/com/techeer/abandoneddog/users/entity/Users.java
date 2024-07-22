package com.techeer.abandoneddog.users.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.techeer.abandoneddog.chatting.domain.UsersChatRoom;
import com.techeer.abandoneddog.global.entity.BaseEntity;
import com.techeer.abandoneddog.users.dto.UserRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE user_id = ?")
@Table(name = "user")
public class Users extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", updatable = false)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@Column(name = "phone_num", nullable = false)
	private String phoneNum;

	@OneToMany(mappedBy = "user")
	private List<UsersChatRoom> usersChatRooms = new ArrayList<>();

	public void update(UserRequestDto dto, PasswordEncoder passwordEncoder) {
		this.username = dto.getUsername();
		//        this.password = passwordEncoder.encode(dto.getPassword());
		this.email = dto.getEmail();
		this.phoneNum = dto.getPhoneNum();
	}
}

