package com.techeer.abandoneddog.global.exception.user;

import com.techeer.abandoneddog.global.exception.ErrorCode;
import com.techeer.abandoneddog.global.exception.MyException;

public class EmailAlreadyExistsException extends MyException {

    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
