package com.ppu.ppu.utils.auth.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum KakaoAuthPurpose {
    LOGIN(System.getenv("KAKAO_REDIRECT_URI")),
    WITHDRAW(System.getenv("WITHDRAW_REDIRECT_URI"));

    private final String redirectUri;
}
