package com.ppu.ppu.exception.domain;

import com.ppu.ppu.exception.BaseException;
import com.ppu.ppu.exception.ErrorCode;

public class UserException extends BaseException {
    public UserException(ErrorCode code) {
        super(code);
    }
}
