package com.techeer.abandoneddog.users.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phoneNum;
}
