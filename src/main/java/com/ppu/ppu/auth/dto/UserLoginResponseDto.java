package com.ppu.ppu.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;
}
