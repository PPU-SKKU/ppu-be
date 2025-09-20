package com.ppu.ppu.exception.domain;

import com.ppu.ppu.exception.BaseException;
import com.ppu.ppu.exception.ErrorCode;

public class ImageException extends BaseException {
    public ImageException(ErrorCode code) {
        super(code);
    }
}
