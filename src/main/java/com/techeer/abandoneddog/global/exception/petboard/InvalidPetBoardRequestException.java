package com.techeer.abandoneddog.global.exception.petboard;

import com.techeer.abandoneddog.global.exception.ErrorCode;
import com.techeer.abandoneddog.global.exception.MyException;

public class InvalidPetBoardRequestException extends MyException {
    public InvalidPetBoardRequestException() {super(ErrorCode.INVALID_PETBOARD_REQUEST); }

}
