package com.ppu.ppu.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class KakaoDTO {
    @Getter
    public static class OAuthToken {
        private String token_type;
        private String access_token;
        private String refresh_token;
        private Integer expires_in;
        private Integer refresh_token_expires_in;
    }

    @Getter
    public static class UserProfile {
        private Long id;

        @JsonProperty("kakao_account")
        private KakaoAccount kakaoAccount;

        @Data
        @Getter
        public static class KakaoAccount {
            private String name;
            private String email;

            private String birthyear;
            private String birthday;
            private String birthday_type;

            private String gender;

            private Profile profile;

            @Data
            @Getter
            public static class Profile {
                private String nickname;
                private String profile_image_url;
            }
        }
    }
}
