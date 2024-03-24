package com.techeer.abandoneddog.users.service;

import com.techeer.abandoneddog.users.dto.LoginRequestDto;
import com.techeer.abandoneddog.users.dto.UserDto;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    public boolean signup(UserDto userDto) {
        Users users = new Users();
        users.setUsername(userDto.getUsername());
        users.setPassword(encoder.encode(userDto.getPassword()));
        users.setEmail(userDto.getEmail());
        users.setPhoneNum(userDto.getPhoneNum());

        try {
            if (!userRepository.existsByEmail(userDto.getEmail())) {
                userRepository.save(users);
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long login(LoginRequestDto loginRequestDto) {
        Users user = loginRequestDto.toEntity();

        try {
            Optional<Users> loginuser = userRepository.findUserByEmail(loginRequestDto.getEmail());
            if (loginuser.isEmpty()) {
                //TODO Execetion 처리
                return null;
            }
            if (encoder.matches(user.getPassword(), loginuser.get().getPassword())) {
                return loginuser.get().getId();
            }
        } catch (Exception e) {
            return null;
        }
        return user.getId();
    }
}

