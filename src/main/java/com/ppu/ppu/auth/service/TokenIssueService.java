package com.ppu.ppu.auth.service;

import com.ppu.ppu.auth.dto.UserLoginResponseDto;
import com.ppu.ppu.auth.dto.UserRefreshResponseDto;
import com.ppu.ppu.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TokenIssueService {
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
