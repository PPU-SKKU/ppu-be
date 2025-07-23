package com.ppu.ppu.auth.service;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.AuthException;
import com.ppu.ppu.user.LoginType;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.user.dto.UserCreateDto;
import com.ppu.ppu.user.dto.UserLoginReponseDto;
import com.ppu.ppu.user.dto.UserPwLoginDto;
import com.ppu.ppu.utils.JwtUtil;
import com.ppu.ppu.utils.KakaoUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final KakaoUtil kakaoUtil;

    public void signup(UserCreateDto user) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), user.getLoginType());
        if (existingUser.isPresent()) {
            throw new AuthException(ErrorCode.AUTH_SIGNUP_FAILED);
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userService.createUser(user);
    }

    public UserLoginReponseDto loginPw(UserPwLoginDto user) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), LoginType.PASSWORD);

        System.out.println("existing user: " + existingUser.isPresent());
        System.out.println("existing user: " + existingUser.get().getLoginType());

        if (!existingUser.isPresent() || !BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword())) {
            throw new AuthException(ErrorCode.AUTH_LOGIN_FAILED);
        }

        String accessToken = jwtUtil.generateToken(existingUser.get().getId().toString());
        return new UserLoginReponseDto(accessToken);
    }

    public UserLoginReponseDto loginOauth(UserCreateDto user) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), user.getLoginType());

        // db has dupliace email but different login type
        /*if(existingUser.isPresent()
                && existingUser.get().getLoginType() != user.getLoginType()) {
            throw new AuthException(ErrorCode.AUTH_SIGNUP_FAILED);
        }*/

        // signup proceed
        if(!existingUser.isPresent()) {
            signup(user);
            existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), user.getLoginType());
        }

        // login proceed
        String accessToken = jwtUtil.generateToken(existingUser.get().getId().toString());
        return new UserLoginReponseDto(accessToken);
    }

}
