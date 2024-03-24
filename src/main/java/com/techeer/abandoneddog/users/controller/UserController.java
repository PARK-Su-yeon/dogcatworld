package com.techeer.abandoneddog.users.controller;

import com.techeer.abandoneddog.users.dto.UserDto;
import com.techeer.abandoneddog.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto) {
        System.out.println(userDto);
        if(userService.signup(userDto)){
            return "redirect:/login";
        }else{
            return "회원가입 실패";
        }
    }
}
