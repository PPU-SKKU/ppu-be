package com.ppu.ppu.utils;

import com.ppu.ppu.utils.config.KakaoConfig;
import com.ppu.ppu.utils.dto.KakaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoUtil {
    private final KakaoConfig kakaoConfig;
    private final WebClient kakaoAuthClient;
    private final WebClient kakaoApiClient;

    public String getAuthorizeCodeUrl() {
        String kakaoAuthorizeUrl = UriComponentsBuilder
                .fromUriString(kakaoConfig.getKAKAO_AUTH_URI())
                .path("/oauth/authorize")
                .queryParam("client_id", kakaoConfig.getCLIENT_ID())
                .queryParam("redirect_uri", kakaoConfig.getREDIRECT_URI())
                .queryParam("response_type", "code")
                .toUriString();

        System.out.println("Kakao Authorize Url: " + kakaoAuthorizeUrl);
        return kakaoAuthorizeUrl;
    }


    public KakaoDTO.OAuthToken requestToken(String accessKey) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getCLIENT_ID());
        params.add("redirect_uri", kakaoConfig.getREDIRECT_URI());
        params.add("code", accessKey);

        return kakaoAuthClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(KakaoDTO.OAuthToken.class)
                .block();
    }

    public KakaoDTO.UserProfile requestUserProfile(String kakaoAccessToken) {
        return kakaoApiClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoDTO.UserProfile.class)
                .block();
    }

    public KakaoDTO.UserUnlink requestUserUnlink(String kakaoAccessToken) {
        return kakaoApiClient.post()
                .uri("/v1/user/unlink")
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoDTO.UserUnlink.class)
                .block();
    }
}
