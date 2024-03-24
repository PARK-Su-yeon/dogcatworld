package com.techeer.abandoneddog.users.service;

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

        try{
            userRepository.save(users);
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
