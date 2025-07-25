package com.ppu.ppu.auth.service;

import com.ppu.ppu.auth.dto.*;
import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.AuthException;
import com.ppu.ppu.user.domain.LoginType;
import com.ppu.ppu.user.domain.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.utils.JwtUtil;
import com.ppu.ppu.utils.KakaoUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenIssueService tokenIssueService;
    private final KakaoUtil kakaoUtil;

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

    public UserLoginResponseDto loginOauth(UserOauthDto user) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), user.getLoginType());

        // signup proceed
        if(existingUser.isEmpty()) {
            signup(new UserCreateDto(user), user.getLoginType());
            existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), user.getLoginType());
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

    public ResponseEntity<Void> withdrawPreHandler(HttpServletRequest request) {
        UUID userId = UUID.fromString((String) request.getAttribute("id"));

        System.out.println("withdraw Id: " + userId);

        Optional<User> user = userService.findUserById(userId);
        if(!user.isPresent()) {
            throw new AuthException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        return switch (user.get().getLoginType()) {
            case PASSWORD -> {
                deleteUserData(userId);
                yield ResponseEntity.ok().build();
            }
            case KAKAO -> ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(kakaoUtil.getAuthorizeCodeUrl()))
                    .build();
            default -> throw new AuthException(ErrorCode.AUTH_INVALID_TOKEN);
        };
    }

    public UUID withdrawOauthUserId(UserOauthDto user) {
        Optional<User> existingUser = userService.getUserByEmailAndLoginType(user.getEmail(), user.getLoginType());

        if(existingUser.isEmpty()) {
            throw new AuthException(ErrorCode.AUTH_INVALID_TOKEN);
        }

        UUID userId = existingUser.get().getId();
        return userId;
    }

    public void deleteUserData(UUID userId) {

    }
}
