package com.ppu.ppu.auth.service;

import com.ppu.ppu.auth.dto.UserOauthDto;
import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.AuthException;
import com.ppu.ppu.user.Gender;
import com.ppu.ppu.user.LoginType;
import com.ppu.ppu.auth.dto.UserLoginResponseDto;
import com.ppu.ppu.utils.auth.kakao.KakaoAuthPurpose;
import com.ppu.ppu.utils.auth.kakao.KakaoUtil;
import com.ppu.ppu.utils.auth.kakao.dto.KakaoOauthInfo;
import com.ppu.ppu.utils.auth.kakao.dto.KakaoUserAuthorizeCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final KakaoUtil kakaoUtil;
    private final AuthService authService;

    public String getKakaoLoginAuthorizeUrl() {
        return kakaoUtil.buildAuthorizeUrl(KakaoAuthPurpose.LOGIN);
    }

    public String getKakaoWithdrawAuthorizeUrl() {
        return kakaoUtil.buildAuthorizeUrl(KakaoAuthPurpose.WITHDRAW);
    }

    public UserLoginResponseDto kakaoLogin(KakaoUserAuthorizeCodeDto dto) {
        KakaoOauthInfo info = kakaoUtil.requestKakaoOauthInfo(dto, KakaoAuthPurpose.LOGIN);
        return authService.loginOauth(mapFromKakaoToUserDto(info));
    }

    public UserOauthDto kakaoWithdraw(KakaoUserAuthorizeCodeDto dto) {
        KakaoOauthInfo info = kakaoUtil.requestKakaoOauthInfo(dto, KakaoAuthPurpose.WITHDRAW);
        kakaoUtil.requestUserUnlink(info.getAccessToken());
        return mapFromKakaoToUserDto(info);
    }

    private UserOauthDto mapFromKakaoToUserDto(KakaoOauthInfo dto) {
        var profile = dto.getProfile().getKakaoAccount();

        if(profile.getEmail() == null) {
            throw new AuthException(ErrorCode.AUTH_OAUTH_NO_EMAIL);
        }

        String nickname = (profile.getProfile() != null) ? profile.getProfile().getNickname() : null;

        String genderStr = profile.getGender();
        Gender gender = (genderStr != null) ? Gender.valueOf(genderStr) : null;

        LocalDate birthday = null;
        if(profile.getBirthday_type() != null) {
            String birthYear = profile.getBirthyear();
            String birthDate = profile.getBirthday();
            String birthType = profile.getBirthday_type();
            birthday = LocalDate.parse(birthYear + "-" + birthDate.substring(0, 2) + "-" + birthDate.substring(2, 4));

            // TODO: birthType에 따른 음력->양력 변경
        }

        return UserOauthDto.builder()
                .email(profile.getEmail())
                .name(profile.getName())
                .nickname(nickname)
                .gender(gender)
                .birth(birthday)
                .loginType(LoginType.KAKAO)
                .build();
    }
}
