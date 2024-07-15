package com.techeer.abandoneddog.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
	private String code;
	private HttpStatus httpStatus;
	private String errorMessage;

	public ErrorResponse(HttpStatus status, String s) {
		this.httpStatus = status;
		this.errorMessage = s;
	}

	public ErrorResponse(ErrorCode code) {
		this.code = code.getCode();
		this.httpStatus = code.getStatus();
		this.errorMessage = code.getMessage();
	}

}
