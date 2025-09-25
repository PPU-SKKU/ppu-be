package com.ppu.ppu.oppu.utils;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.OppuException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateParser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new OppuException(ErrorCode.OPPU_INVALID_DATE_FORMAT);
        }
    }
}
