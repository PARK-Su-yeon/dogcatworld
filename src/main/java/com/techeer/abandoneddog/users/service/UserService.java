package com.techeer.abandoneddog.users.service;

import com.techeer.abandoneddog.users.dto.LoginRequestDto;
import com.techeer.abandoneddog.users.dto.LoginResponseDto;
import com.techeer.abandoneddog.users.dto.RegisterRequestDto;

public interface UserService {

    void signUp(RegisterRequestDto registerRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);
}
