package com.techeer.abandoneddog.users.dto;

import com.techeer.abandoneddog.pet_info.entity.PetInfo;
import com.techeer.abandoneddog.users.entity.Users;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegisterRequestDto {
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
