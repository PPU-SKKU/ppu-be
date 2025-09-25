package com.ppu.ppu.exception.domain;

import com.ppu.ppu.exception.BaseException;
import com.ppu.ppu.exception.ErrorCode;

public class OppuException extends BaseException {
    public OppuException(ErrorCode code) {
        super(code);
    }
}
