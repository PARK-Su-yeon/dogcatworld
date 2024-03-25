package com.techeer.abandoneddog.global.exception.user;

import com.techeer.abandoneddog.global.exception.ErrorCode;
import com.techeer.abandoneddog.global.exception.MyException;

public class InvalidPasswordException extends MyException {

    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }
}