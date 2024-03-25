package com.techeer.abandoneddog.global.exception.user;

import com.techeer.abandoneddog.global.exception.ErrorCode;
import com.techeer.abandoneddog.global.exception.MyException;

public class UserNotFoundException extends MyException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}