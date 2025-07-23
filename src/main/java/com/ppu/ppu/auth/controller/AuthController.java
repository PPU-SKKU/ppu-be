package com.ppu.ppu.auth.controller;


import com.ppu.ppu.auth.service.AuthService;
import com.ppu.ppu.auth.service.OAuthService;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.user.dto.UserCreateDto;
import com.ppu.ppu.user.dto.UserLoginReponseDto;
import com.ppu.ppu.user.dto.UserPwLoginDto;
import com.ppu.ppu.utils.JwtUtil;
import com.ppu.ppu.utils.KakaoUtil;
import com.ppu.ppu.utils.dto.KakaoDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final KakaoUtil kakaoUtil;
    private final OAuthService oAuthService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody UserCreateDto user) {
        authService.signup(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginReponseDto> login(@Valid @RequestBody UserPwLoginDto user, HttpServletRequest request) {
        return ResponseEntity.ok().body(authService.loginPw(user));
    }

    @GetMapping("/login/kakao")
    public ResponseEntity<Void> kakaoLogin() {
        return ResponseEntity.status(302)
                .location(URI.create(kakaoUtil.getAuthorizeCodeUrl()))
                .build();
    }

    @GetMapping("/login/kakao/callback")
    public ResponseEntity<UserLoginReponseDto> kakaoLoginCallback(@Valid @ModelAttribute KakaoDTO.AuthorizeCode dto, HttpServletRequest request) {
        return ResponseEntity.ok().body(oAuthService.kakaoLogin(dto));
    }


}
