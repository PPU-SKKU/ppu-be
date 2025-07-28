package com.ppu.ppu.auth.service;

import com.ppu.ppu.auth.dto.*;
import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.AuthException;
import com.ppu.ppu.user.domain.LoginType;
import com.ppu.ppu.user.domain.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.utils.JwtUtil;
import com.ppu.ppu.utils.TokenIssueUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenIssueUtil tokenIssueService;

    public void signup(UserCreateDto user, LoginType loginType) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), loginType);
        if (existingUser.isPresent()) {
            throw new AuthException(ErrorCode.AUTH_SIGNUP_FAILED);
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userService.createUser(user, loginType);
    }

    public UserLoginResponseDto loginPw(UserPwLoginDto user) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), LoginType.PASSWORD);

        if (existingUser.isEmpty() || !BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword())) {
            throw new AuthException(ErrorCode.AUTH_LOGIN_FAILED);
        }

        return tokenIssueService.issueAllToken(existingUser.get().getId());
    }

    public UserLoginResponseDto loginOauth(UserCreateDto user, LoginType loginType) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), loginType);

        // signup proceed
        if(existingUser.isEmpty()) {
            signup(user, loginType);
            existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), loginType);
        }

        // login proceed
        return tokenIssueService.issueAllToken(existingUser.get().getId());
    }

    public UserRefreshResponseDto refresh(UserRefreshDto dto) {
        String refreshToken = dto.getRefreshToken();
        if(!jwtUtil.validateToken(refreshToken)) {
            throw new AuthException(ErrorCode.AUTH_EXPIRED_TOKEN);
        }

        UUID id = UUID.fromString(jwtUtil.parseToken(refreshToken));
        if(userService.findUserById(id).isEmpty()) {
            throw new AuthException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        return tokenIssueService.issueAccessToken(id);
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
