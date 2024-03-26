package com.techeer.abandoneddog.users.service;

import com.techeer.abandoneddog.global.exception.user.EmailAlreadyExistsException;
import com.techeer.abandoneddog.global.exception.user.InvalidPasswordException;
import com.techeer.abandoneddog.global.exception.user.UserNotFoundException;
import com.techeer.abandoneddog.users.dto.*;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    @Transactional
    public void signUp(RegisterRequestDto requestDto) {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        Users user = requestDto.toEntity(encoder);
        userRepository.save(user);
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public Users getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
    }

    @Override
    @Transactional
    public Page<UserResponseDto> getUsers(Pageable pageable) {
        Page<Users> usersPage = userRepository.findAll(pageable);
        return usersPage.map(UserResponseDto::fromEntity);
    }

    @Override
    @Transactional
    public Object updateUser(UserRequestDto dto, Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        user.update(dto, encoder);
        return UserResponseDto.fromEntity(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Users user = userRepository.findUserById(userId).get();
        userRepository.deleteById(user.getId());
    }
}

