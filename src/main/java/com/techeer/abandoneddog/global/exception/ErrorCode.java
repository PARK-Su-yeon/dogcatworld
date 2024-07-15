package com.techeer.abandoneddog.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "유저를 찾을 수 없습니다."),
	EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "U0002", "이미 존재하는 이메일입니다."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "유효하지 않은 비밀번호입니다."),

	PETBOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "게시글을 찾을 수 없습니다."),
	INVALID_PETBOARD_REQUEST(HttpStatus.BAD_REQUEST, "P002", "유효하지 않은 게시글입니다.");

	private HttpStatus status;
	private String code;
	private String message;

	ErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

}
