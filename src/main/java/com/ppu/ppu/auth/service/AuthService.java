package com.ppu.ppu.auth.service;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.AuthException;
import com.ppu.ppu.user.LoginType;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.auth.dto.UserCreateDto;
import com.ppu.ppu.auth.dto.UserLoginReponseDto;
import com.ppu.ppu.auth.dto.UserPwLoginDto;
import com.ppu.ppu.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public void signup(UserCreateDto user, LoginType loginType) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), loginType);
        if (existingUser.isPresent()) {
            throw new AuthException(ErrorCode.AUTH_SIGNUP_FAILED);
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userService.createUser(user, loginType);
    }

    public UserLoginReponseDto loginPw(UserPwLoginDto user) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), LoginType.PASSWORD);

        if (!existingUser.isPresent() || !BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword())) {
            throw new AuthException(ErrorCode.AUTH_LOGIN_FAILED);
        }

        String accessToken = jwtUtil.generateToken(existingUser.get().getId().toString());
        return new UserLoginReponseDto(accessToken);
    }

    public UserLoginReponseDto loginOauth(UserCreateDto user, LoginType loginType) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), loginType);

        // signup proceed
        if(!existingUser.isPresent()) {
            signup(user, loginType);
            existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), loginType);
        }

        // login proceed
        String accessToken = jwtUtil.generateToken(existingUser.get().getId().toString());
        return new UserLoginReponseDto(accessToken);
    }

    /*public void withdraw(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if(token == null || !token.startsWith("Bearer ")) {
            throw new AuthException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        token = token.substring(7);
        if(!jwtUtil.validateToken(token)) {
            throw new AuthException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        System.out.println("withdraw token: " + token);
        String Id = jwtUtil.parseToken(token);

        System.out.println("withdraw Id: " + Id);

        Optional<User> user = userService.findUserById(Id);
        if(!user.isPresent()) {
            throw new AuthException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        // Login Type에 따라서 먼저 OAuth의 계정을 삭제

    }*/
}
