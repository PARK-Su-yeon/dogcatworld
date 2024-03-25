package com.techeer.abandoneddog.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Builder
public class ResultDto<T> {
    private HttpStatus status;
    private String message;
    private T result;

    public ResultDto(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
        this.result = null;
    }

    public static<T> ResultDto<T> res(final HttpStatus status, final String message) {
        return res(status, message, null);
    }

    public static<T> ResultDto<T> res(final HttpStatus status, final String message, final T t) {
        return ResultDto.<T>builder()
                .result(t)
                .status(status)
                .message(message)
                .build();
    }
}
