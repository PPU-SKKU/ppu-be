package com.ppu.ppu.auth.controller;


import com.ppu.ppu.auth.dto.*;
import com.ppu.ppu.auth.service.AuthService;
import com.ppu.ppu.auth.service.OAuthService;
import com.ppu.ppu.user.domain.LoginType;
import com.ppu.ppu.utils.KakaoUtil;
import com.ppu.ppu.utils.dto.KakaoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final KakaoUtil kakaoUtil;
    private final OAuthService oAuthService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody UserCreateDto user) {
        authService.signup(user, LoginType.PASSWORD);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserPwLoginDto user) {
        return ResponseEntity.ok().body(authService.loginPw(user));
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<Void> kakaoLogin() {
        return ResponseEntity.status(302)
                .location(URI.create(kakaoUtil.getAuthorizeCodeUrl()))
                .build();
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<UserLoginResponseDto> kakaoLoginCallback(@Valid @ModelAttribute KakaoDTO.AuthorizeCode dto) {
        return ResponseEntity.ok().body(oAuthService.kakaoLogin(dto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserRefreshResponseDto> refresh(@Valid @RequestBody UserRefreshDto dto) {
        return ResponseEntity.ok().body(authService.refresh(dto));
    }

    /*@DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(HttpServletRequest request) {
        authService.withdraw(request);
        return ResponseEntity.ok().build();
    }*/
}
