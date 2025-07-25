package com.ppu.ppu.utils.kakao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoOauthInfo {
    String accessToken;
    KakaoUserProfileDto profile;
}