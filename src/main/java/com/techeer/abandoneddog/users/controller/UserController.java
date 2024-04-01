package com.techeer.abandoneddog.users.controller;

import com.techeer.abandoneddog.users.dto.*;
import com.techeer.abandoneddog.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/")
public class UserController implements UserControllerDocs{
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {
        userService.signUp(registerRequestDto);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto user = userService.login(loginRequestDto);
        return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "로그인 성공", user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        try {
            UserResponseDto responseDto = userService.getUser(id);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "유저 조회 성공", responseDto));
        } catch (Exception e) {
            return ResponseEntity.ok(ResultDto.res(HttpStatus.BAD_REQUEST, "유저 조회애 실패했습니다."));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getUsers(Pageable pageable) {
        try {
            Page<UserResponseDto> usersPage = userService.getUsers(pageable);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "유저 리스트 조회 성공", usersPage.getContent()));
        } catch (Exception e) {
            return ResponseEntity.ok(ResultDto.res(HttpStatus.BAD_REQUEST, "유저 리스트 조회애 실패했습니다."));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
        try {
            UserResponseDto responseDto = userService.updateUser(id, userRequestDto);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "유저 정보 수정 성공", responseDto));
        } catch (Exception e) {
            return ResponseEntity.ok(ResultDto.res(HttpStatus.BAD_REQUEST, "유저 정보 수정애 실패했습니다."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ResultDto.res(HttpStatus.OK, "유저 삭제 성공"));
        } catch (Exception e) {
            return ResponseEntity.ok(ResultDto.res(HttpStatus.BAD_REQUEST, "유저 삭제애 실패했습니다."));
        }
    }
}
