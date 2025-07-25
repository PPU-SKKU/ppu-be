package com.ppu.ppu.auth.service;

import com.ppu.ppu.auth.dto.*;
import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.AuthException;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.user.domain.User;
import com.ppu.ppu.utils.kakao.dto.KakaoUserAuthorizeCodeDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final UserService userService;
    private final OAuthService oAuthService;

    public ResponseEntity<Void> withdrawPreHandler(HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));

        User user = userService.findUserById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_INVALID_TOKEN));

        return switch (user.getLoginType()) {
            case PASSWORD -> {
                userService.deleteUser(userId);
                yield ResponseEntity.noContent().build();
            }
            case KAKAO -> ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(oAuthService.getKakaoWithdrawAuthorizeUrl()))
                    .build();
            default -> throw new AuthException(ErrorCode.AUTH_INVALID_TOKEN);
        };
    }

    public void withdrawKakao(KakaoUserAuthorizeCodeDto dto) {
        UserOauthDto oauthDto = oAuthService.kakaoWithdraw(dto);
        User user = userService.getUserByEmailAndLoginType(oauthDto.getEmail(), oauthDto.getLoginType())
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_INVALID_TOKEN));

        UUID userId = user.getId();
        userService.deleteUser(userId);
    }
}
