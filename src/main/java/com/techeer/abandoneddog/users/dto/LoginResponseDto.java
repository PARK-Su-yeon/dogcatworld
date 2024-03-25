package com.techeer.abandoneddog.users.dto;

import com.techeer.abandoneddog.users.entity.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private Long userId;
    private String email;

    public static LoginResponseDto fromEntity(Users users) {
        return LoginResponseDto.builder()
                .userId(users.getId())
                .email(users.getEmail())
                .build();
    }
}