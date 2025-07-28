package com.ppu.ppu.utils;

import com.ppu.ppu.auth.dto.UserLoginResponseDto;
import com.ppu.ppu.auth.dto.UserRefreshResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenIssueUtil {
    private final JwtUtil jwtUtil;

    public UserLoginResponseDto issueAllToken(UUID userId) {
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId);
        return new UserLoginResponseDto(accessToken, refreshToken);
    }

    public UserRefreshResponseDto issueAccessToken(UUID userId) {
        String accessToken = jwtUtil.generateAccessToken(userId);
        return new UserRefreshResponseDto(accessToken);
    }
}
