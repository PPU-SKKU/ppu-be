package com.ppu.ppu.utils.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Getter
public class KakaoConfig {

    private final String KAKAO_AUTH_URI = "https://kauth.kakao.com";
    private final String KAKAO_API_URI = "https://kapi.kakao.com";
    private final String CLIENT_ID;
    private final String REDIRECT_URI;

    public KakaoConfig() {
        CLIENT_ID = System.getenv("KAKAO_REST_API_KEY");
        REDIRECT_URI = System.getenv("KAKAO_REDIRECT_URI");
    }

    @Bean
    public WebClient kakaoAuthClient(WebClient.Builder builder) {
        return builder
                .baseUrl(KAKAO_AUTH_URI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean
    public WebClient kakaoApiClient(WebClient.Builder builder) {
        return builder
                .baseUrl(KAKAO_API_URI)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
