package com.ppu.ppu.utils.kakao;

import com.ppu.ppu.utils.kakao.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KakaoUtil {
    private final KakaoConfig kakaoConfig;
    private final WebClient kakaoAuthClient;
    private final WebClient kakaoApiClient;

    public String buildAuthorizeUrl(KakaoAuthPurpose purpose) {
        return UriComponentsBuilder
                .fromUriString(kakaoConfig.getKAKAO_AUTH_URI())
                .path("/oauth/authorize")
                .queryParam("client_id", kakaoConfig.getCLIENT_ID())
                .queryParam("redirect_uri", purpose.getRedirectUri())
                .queryParam("response_type", "code")
                .toUriString();
    }

    public KakaoOauthInfo requestKakaoOauthInfo(KakaoUserAuthorizeCodeDto dto, KakaoAuthPurpose purpose) {
        String code = dto.getCode();

        KakaoUserAccessTokenDto token = requestToken(code, purpose);
        KakaoUserProfileDto profile = requestUserProfile(token.getAccess_token());

        return new KakaoOauthInfo(token.getAccess_token(), profile);
    }

    private KakaoUserAccessTokenDto requestToken(String code, KakaoAuthPurpose purpose) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoConfig.getCLIENT_ID());
        params.add("redirect_uri", purpose.getRedirectUri());
        params.add("code", code);

        return kakaoAuthClient.post()
                .uri("/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("Error response from Kakao: " + errorBody);
                                    return Mono.error(new RuntimeException("Kakao token request failed: " + errorBody));
                                })
                )
                .bodyToMono(KakaoUserAccessTokenDto.class)
                .block();
    }

    private KakaoUserProfileDto requestUserProfile(String kakaoAccessToken) {
        return kakaoApiClient.get()
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    System.err.println("Error response from Kakao: " + errorBody);
                                    return Mono.error(new RuntimeException("Kakao token request failed: " + errorBody));
                                })
                )
                .bodyToMono(KakaoUserProfileDto.class)
                .block();
    }

    public KakaoUserUnlinkDto requestUserUnlink(String kakaoAccessToken) {
        return kakaoApiClient.post()
                .uri("/v1/user/unlink")
                .header("Authorization", "Bearer " + kakaoAccessToken)
                .retrieve()
                .bodyToMono(KakaoUserUnlinkDto.class)
                .block();
    }
}
