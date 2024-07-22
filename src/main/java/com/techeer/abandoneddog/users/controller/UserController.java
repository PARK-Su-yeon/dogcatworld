package com.techeer.abandoneddog.users.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techeer.abandoneddog.users.dto.LoginRequestDto;
import com.techeer.abandoneddog.users.dto.LoginResponseDto;
import com.techeer.abandoneddog.users.dto.RegisterRequestDto;
import com.techeer.abandoneddog.users.dto.ResultDto;
import com.techeer.abandoneddog.users.dto.UserRequestDto;
import com.techeer.abandoneddog.users.dto.UserResponseDto;
import com.techeer.abandoneddog.users.service.ReissueService;
import com.techeer.abandoneddog.users.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/")
@Validated
public class UserController implements UserControllerDocs {
	@Autowired
	private final UserService userService;
	private final ReissueService reissueService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
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

	@PostMapping("/reissue")
	public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
		try {
			Map<String, String> newTokens = reissueService.reissueTokens(request, response);
			if (newTokens.containsKey("error")) {
				return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(ResultDto.res(HttpStatus.BAD_REQUEST, newTokens.get("error")));
			} else {
				return ResponseEntity
					.ok(ResultDto.res(HttpStatus.OK, "토큰 재발급 성공", newTokens));
			}
		} catch (Exception e) {
			return ResponseEntity.ok(ResultDto.res(HttpStatus.BAD_REQUEST, "토큰 재발급애 실패했습니다."));
		}
	}
}
