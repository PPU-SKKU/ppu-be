package com.ppu.ppu.auth.service;

import com.ppu.ppu.exception.ErrorCode;
import com.ppu.ppu.exception.domain.AuthException;
import com.ppu.ppu.user.Gender;
import com.ppu.ppu.user.LoginType;
import com.ppu.ppu.user.dto.UserCreateDto;
import com.ppu.ppu.user.dto.UserLoginReponseDto;
import com.ppu.ppu.utils.KakaoUtil;
import com.ppu.ppu.utils.dto.KakaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final KakaoUtil kakaoUtil;
    private final AuthService authService;

    public UserLoginReponseDto kakaoLogin(KakaoDTO.AuthorizeCode dto) {
        // extract code
        String code = dto.getCode();
        System.out.println("Kakao User Code: " + code);

        // receive kakao access token
        KakaoDTO.OAuthToken token;
        try {
            token = kakaoUtil.requestToken(code);
        } catch (Exception e) {
            throw new AuthException(ErrorCode.AUTH_OAUTH_KAKAO_API_FAILED);
        }
        String kakaoAccessToken = token.getAccess_token();
        System.out.println("Kakao User Token: " + kakaoAccessToken);

        // Kakao에서 정보 추출
        // 이미 유저가 존재하는 경우 email과 LoginType만 사용
        // 새로 유저를 signup하는 경우는 모든 정보 사용
        KakaoDTO.UserProfile profile;
        try {
             profile = kakaoUtil.requestUserProfile(kakaoAccessToken);
        } catch (Exception e) {
            throw new AuthException(ErrorCode.AUTH_OAUTH_KAKAO_API_FAILED);
        }


        UserCreateDto user = new UserCreateDto();

        // 1. email
        try {
            String email = profile.getKakaoAccount().getEmail();
            user.setEmail(email);
        } catch (Exception e) {
            throw new AuthException(ErrorCode.AUTH_OAUTH_NO_EMAIL);
        }

        // 2. name
        // TODO: test for name
        try {
            String name = profile.getKakaoAccount().getName();
            user.setName(name);
        } catch (Exception ignore) {}

        // 3. gender
        // TODO: test for gender
        try {
            String gender = profile.getKakaoAccount().getGender();
            Gender genderEnum = (gender == null) ? null : Gender.valueOf(gender);
            user.setGender(genderEnum);
        } catch (Exception ignore) {}

        // 4. nickname
        try {
            String nickname = profile.getKakaoAccount().getProfile().getNickname();
            user.setNickname(nickname);
        } catch (Exception ignore) {}

        // 5. birth
        // TODO: test for birth
        try {
            String birthYear = profile.getKakaoAccount().getBirthyear();
            String birthDate = profile.getKakaoAccount().getBirthday();
            String birthType = profile.getKakaoAccount().getBirthday_type();

            LocalDate birthday = null;
            if (birthType != null) {
                birthday = LocalDate.parse(birthYear + "-" + birthDate.substring(0, 2) + "-" + birthDate.substring(2, 4));
                // TODO: birthType에 따른 음력->양력 변경
            }
            user.setBirth(birthday);
        } catch (Exception ignore) {}

        // 6. LoginType
        user.setLoginType(LoginType.KAKAO);

        return authService.loginOauth(user);
    }
}
