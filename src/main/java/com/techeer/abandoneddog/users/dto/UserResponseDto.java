package com.techeer.abandoneddog.users.dto;

import com.techeer.abandoneddog.users.entity.Users;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
	private Long id;

	private String username;

	private String email;

	private String password;

	private String phoneNum;

	public static UserResponseDto fromEntity(Users users) {
		return new UserResponseDto(
			users.getId(),
			users.getUsername(),
			users.getEmail(),
			users.getPassword(),
			users.getPhoneNum()
		);
	}
}
