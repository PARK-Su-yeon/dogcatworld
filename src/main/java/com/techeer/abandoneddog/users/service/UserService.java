package com.techeer.abandoneddog.users.service;

import com.techeer.abandoneddog.users.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void signUp(RegisterRequestDto registerRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    void deleteUser(Long id);

    UserResponseDto getUser(Long id);

    Page<UserResponseDto> getUsers(Pageable pageable);

    UserResponseDto updateUser(Long id, UserRequestDto dto);

}
