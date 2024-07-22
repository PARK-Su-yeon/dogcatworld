package com.techeer.abandoneddog.global.exception.petboard;

import com.techeer.abandoneddog.global.exception.ErrorCode;
import com.techeer.abandoneddog.global.exception.MyException;

public class PetBoardNotFoundException extends MyException {
	public PetBoardNotFoundException() {
		super(ErrorCode.PETBOARD_NOT_FOUND);
	}
}