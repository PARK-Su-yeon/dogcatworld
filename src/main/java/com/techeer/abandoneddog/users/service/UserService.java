package com.techeer.abandoneddog.users.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.techeer.abandoneddog.users.dto.LoginRequestDto;
import com.techeer.abandoneddog.users.dto.LoginResponseDto;
import com.techeer.abandoneddog.users.dto.RegisterRequestDto;
import com.techeer.abandoneddog.users.dto.UserRequestDto;
import com.techeer.abandoneddog.users.dto.UserResponseDto;

public interface UserService {

	void signUp(RegisterRequestDto registerRequestDto);

	LoginResponseDto login(LoginRequestDto loginRequestDto);

	void deleteUser(Long id);

	UserResponseDto getUser(Long id);

	Page<UserResponseDto> getUsers(Pageable pageable);

	UserResponseDto updateUser(Long id, UserRequestDto dto);

}
