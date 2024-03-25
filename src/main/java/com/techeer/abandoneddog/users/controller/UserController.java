package com.techeer.abandoneddog.users.controller;

import com.techeer.abandoneddog.users.dto.LoginRequestDto;
import com.techeer.abandoneddog.users.dto.RegisterRequestDto;
import com.techeer.abandoneddog.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
        userService.signUp(registerRequestDto);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        Long uid = userService.login(loginRequestDto);
        return ResponseEntity.ok().body("로그인 성공");
    }
}
