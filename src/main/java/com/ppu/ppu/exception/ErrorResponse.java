package com.ppu.ppu.exception;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String code,
        String message,
        LocalDateTime timestamp,
        List<ValidErrorDetail> errors
) {
    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(
                code.getStatus().value(),
                code.name(),
                code.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    public static ErrorResponse of(ErrorCode code, List<ValidErrorDetail> errors) {
        return new ErrorResponse(
                code.getStatus().value(),
                code.name(),
                code.getMessage(),
                LocalDateTime.now(),
                errors
        );
    }
}
