package com.techeer.abandoneddog.users.service;

import com.techeer.abandoneddog.global.exception.user.EmailAlreadyExistsException;
import com.techeer.abandoneddog.global.exception.user.InvalidPasswordException;
import com.techeer.abandoneddog.global.exception.user.UserNotFoundException;
import com.techeer.abandoneddog.users.dto.LoginRequestDto;
import com.techeer.abandoneddog.users.dto.LoginResponseDto;
import com.techeer.abandoneddog.users.dto.RegisterRequestDto;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public void signUp(RegisterRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        Users user = requestDto.toEntity(encoder);
        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Users user = loginRequestDto.toEntity();
        Optional<Users> loginuser = userRepository.findUserByEmail(loginRequestDto.getEmail());

        if (loginuser.isEmpty()) {
            throw new UserNotFoundException();
        }

        if (!encoder.matches(user.getPassword(), loginuser.get().getPassword())) {
            throw new InvalidPasswordException();
        }

        return LoginResponseDto.builder()
                .userId(loginuser.get().getId())
                .email(loginuser.get().getEmail())
                .build();
    }
}

