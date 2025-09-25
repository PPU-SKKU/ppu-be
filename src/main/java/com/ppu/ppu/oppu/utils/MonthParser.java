package com.ppu.ppu.oppu.utils;

import com.ppu.ppu.exception.domain.OppuException;
import com.ppu.ppu.exception.ErrorCode;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MonthParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    public static YearMonth parseMonth(String monthStr) {
        try {
            return YearMonth.parse(monthStr, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new OppuException(ErrorCode.OPPU_INVALID_MONTH_FORMAT);
        }
    }
}
