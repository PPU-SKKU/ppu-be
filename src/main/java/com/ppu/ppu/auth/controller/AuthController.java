package com.ppu.ppu.auth.controller;


import com.ppu.ppu.auth.dto.*;
import com.ppu.ppu.auth.service.AuthService;
import com.ppu.ppu.auth.service.OAuthService;
import com.ppu.ppu.auth.service.WithdrawService;
import com.ppu.ppu.utils.kakao.dto.KakaoUserAuthorizeCodeDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "auth", description = "인증 및 인가 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final OAuthService oAuthService;
    private final WithdrawService withdrawService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody UserCreateDto user) {
        authService.signup(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserPwLoginDto user) {
        return ResponseEntity.ok(authService.loginPw(user));
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<Void> kakaoLogin() {
        String uri = oAuthService.getKakaoLoginAuthorizeUrl();
        return ResponseEntity.status(302)
                .location(URI.create(uri))
                .build();
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<UserLoginResponseDto> kakaoLoginCallback(@Valid @ModelAttribute KakaoUserAuthorizeCodeDto dto) {
        return ResponseEntity.ok(oAuthService.kakaoLogin(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserRefreshResponseDto> refresh(@Valid @RequestBody UserRefreshDto dto) {
        return ResponseEntity.ok(authService.refresh(dto));
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> startWithdraw(HttpServletRequest request) {
        return withdrawService.withdrawPreHandler(request);
    }

    @GetMapping("/withdraw/kakao/callback")
    public ResponseEntity<Void> kakaoWithdrawCallback(@Valid @ModelAttribute KakaoUserAuthorizeCodeDto dto) {
        withdrawService.withdrawKakao(dto);
        return ResponseEntity.noContent().build();
    }
}
