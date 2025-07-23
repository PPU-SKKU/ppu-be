package com.ppu.ppu.exception.domain;

import com.ppu.ppu.exception.BaseException;
import com.ppu.ppu.exception.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode code) {
        super(code);
    }
}
