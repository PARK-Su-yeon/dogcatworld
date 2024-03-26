package com.techeer.abandoneddog.users.dto;

import com.techeer.abandoneddog.users.entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequestDto {

    @NotBlank(message = "유저이름을 입력하세요.")
    private String username;

    @NotBlank(message = "이메일 주소를 입력하세요.")
    @Email(message = "올바른 이메일 주소를 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력하세요.")
    private String password;

    @NotBlank(message = "휴대폰 번호를 입력하세요.")
    private String phoneNum;

    @Builder
    public Users toEntity(PasswordEncoder passwordEncoder) {
        return Users.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .phoneNum(phoneNum)
                .build();
    }

}
