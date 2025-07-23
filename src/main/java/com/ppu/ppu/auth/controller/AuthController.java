package com.ppu.ppu.auth.controller;


import com.ppu.ppu.auth.dto.KakaoDTO;
import com.ppu.ppu.user.LoginType;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.user.dto.UserCreateDto;
import com.ppu.ppu.user.dto.UserLoginReponseDto;
import com.ppu.ppu.user.dto.UserPwLoginDto;
import com.ppu.ppu.utils.JwtUtil;
import com.ppu.ppu.utils.KakaoUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final KakaoUtil kakaoUtil;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody UserCreateDto user) {
        Optional<User> existingUser = this.userService.getUserByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            // 가입 불가
            return ResponseEntity.badRequest().build();
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userService.createUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/kakao")
    public ResponseEntity<UserLoginReponseDto> kakaoLogin(HttpServletRequest request) {
        // bearer 제거
        String code = accessKey.substring(7);

        // token 발급
        KakaoDTO.OAuthToken token = kakaoUtil.requestOAuthToken(code);

        // 발급된 token으로 정보 추출
        KakaoDTO.UserProfile profile = kakaoUtil.requestUserProfile(token.getAccess_token());

        // email을 못불러왔으면 실패
        String email = profile.getKakaoAccount().getEmail();
        if(email == null) {
            return ResponseEntity.badRequest().build();
        }

        // email로 이미 가입한 적이 있으면, 그 계정으로 로그인
        Optional<User> existingUser = this.userService.getUserByEmail(email);
        if(existingUser.isPresent() && existingUser.get().getLoginType() != LoginType.KAKAO) {
            return ResponseEntity.badRequest().build();
        }
        else if (existingUser.isPresent()) {
            String accessToken = jwtUtil.generateToken(existingUser.get().getId().toString());
            return ResponseEntity.status(200).body(new UserLoginReponseDto(accessToken));
        }

        // email로 가입한 적 없으면 signup
        UserCreateDto user = new UserCreateDto();
        user.setLoginType(LoginType.KAKAO);
        user.setEmail(email);

        // name
        if(profile.getKakaoAccount().getName() != null) {
            user.setName(profile.getKakaoAccount().getName());
        }

        // nickname
        if(profile.getKakaoAccount().getProfile().getNickname() != null) {
            user.setName(profile.getKakaoAccount().getProfile().getNickname());
        }

        // TODO: Gender

        // TODO: Birthday

        // TODO: profile_image

        userService.createUser(user);

        Optional<User> signupUser = this.userService.getUserByEmail(user.getEmail());
        String accessToken = jwtUtil.generateToken(signupUser.get().getId().toString());
        return ResponseEntity.status(200).body(new UserLoginReponseDto(accessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginReponseDto> login(@RequestBody UserPwLoginDto user, HttpServletRequest request) {
        Optional<User> existingUser = this.userService.getUserByEmail(user.getEmail());

        if (!existingUser.isPresent() || !BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        String accessToken = jwtUtil.generateToken(existingUser.get().getId().toString());
        return ResponseEntity.status(200).body(new UserLoginReponseDto(accessToken));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authHeader.substring(7);

        if(!jwtUtil.validateToken(token)) {
            return ResponseEntity.badRequest().build();
        }

        String userId = jwtUtil.parseToken(token);

        // TODO: DELETE
        // Kakao의 Access Token은 어디에 저장하지?

        return ResponseEntity.ok().build();
    }

    /*@PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }

        String token = authHeader.substring(7);


    }*/
}
