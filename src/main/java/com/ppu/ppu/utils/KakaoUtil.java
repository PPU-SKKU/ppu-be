package com.ppu.ppu.utils;

import com.ppu.ppu.auth.dto.KakaoDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
public class KakaoUtil {

    private final String client_id;
    private final String redirect_uri;

    public KakaoUtil() {
        client_id = System.getenv("KAKAO_CLIENT_ID");
        redirect_uri = System.getenv("KAKAO_REDIRECT_URI");
    }

    private final WebClient tokenClient = WebClient.builder()
            .baseUrl("https://kauth.kakao.com")
            .build();

    private final WebClient profileClient = WebClient.builder()
            .baseUrl("https://kapi.kakao.com")
            .build();

    public KakaoDTO.OAuthToken requestOAuthToken(String accessKey) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirect_uri);
        params.add("code", accessKey);

        System.out.println("Kakao Token Request Body Parameters:");
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            for (String value : entry.getValue()) {
                System.out.println("  " + entry.getKey() + "=" + value);
            }
        }

        return tokenClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(KakaoDTO.OAuthToken.class)
                .block();
    }

    public KakaoDTO.UserProfile requestUserProfile(String accessToken) {
        return profileClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(KakaoDTO.UserProfile.class)
                .block();
    }
}
