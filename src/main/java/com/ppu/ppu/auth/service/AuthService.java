package com.ppu.ppu.auth.service;

import com.ppu.ppu.auth.dto.*;
import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.AuthException;
import com.ppu.ppu.user.LoginType;
import com.ppu.ppu.user.User;
import com.ppu.ppu.user.UserService;
import com.ppu.ppu.utils.JwtUtil;
import com.ppu.ppu.utils.TokenIssueUtil;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenIssueUtil tokenIssueService;

    public void signup(UserCreateDto dto) {
        if(userService.existsUserByEmailAndLoginType(dto.getEmail(), LoginType.PASSWORD)) {
            throw new AuthException(ErrorCode.AUTH_SIGNUP_FAILED);
        }

        String hashedPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        dto.setPassword(hashedPassword);
        userService.createUser(dto, LoginType.PASSWORD);
    }

    public UserLoginResponseDto loginPw(UserPwLoginDto dto) {
        User user = userService.getUserByEmailAndLoginType(dto.getEmail(), LoginType.PASSWORD)
                .orElseThrow(() -> new AuthException(ErrorCode.AUTH_LOGIN_FAILED));

        return tokenIssueService.issueAllToken(user.getId());
    }

    public UserLoginResponseDto loginOauth(UserOauthDto dto) {
        User user = userService.getUserByEmailAndLoginType(dto.getEmail(), dto.getLoginType())
                .orElseGet(() -> userService.createUser(mapFromUserOauthToUserCreateDto(dto), dto.getLoginType()));

        return tokenIssueService.issueAllToken(user.getId());
    }

    private UserCreateDto mapFromUserOauthToUserCreateDto(UserOauthDto dto) {
        return UserCreateDto.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .birth(dto.getBirth())
                .gender(dto.getGender())
                .build();
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
}
