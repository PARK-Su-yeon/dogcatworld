package com.techeer.abandoneddog.users.service;

import com.techeer.abandoneddog.users.dto.UserDto;
import com.techeer.abandoneddog.users.entity.Users;
import com.techeer.abandoneddog.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean signup(UserDto userDto) {
        Users users = new Users();
        users.setUsername(userDto.getUsername());
        users.setPassword(userDto.getPassword());
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
