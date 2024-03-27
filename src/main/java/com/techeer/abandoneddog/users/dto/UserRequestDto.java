package com.techeer.abandoneddog.users.dto;

import com.techeer.abandoneddog.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    private String phoneNum;

    public Users toEntity() {
        return Users.builder()
                .username(username)
                .email(email)
                .password(password)
                .phoneNum(phoneNum)
                .build();
    }
}
