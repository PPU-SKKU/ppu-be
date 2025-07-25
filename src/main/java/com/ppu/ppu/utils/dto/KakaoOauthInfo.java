package com.ppu.ppu.utils.dto;


public record KakaoOauthInfo(
        String accessToken,
        KakaoDTO.UserProfile profile
) {}