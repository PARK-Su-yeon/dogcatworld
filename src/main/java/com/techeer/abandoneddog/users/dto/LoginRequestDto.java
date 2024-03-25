package com.techeer.abandoneddog.users.dto;

import com.techeer.abandoneddog.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class LoginRequestDto {

    private String email;
    private String password;

    public Users toEntity() {
        return Users.builder()
                .email(email)
                .password(password)
                .build();
    }
}