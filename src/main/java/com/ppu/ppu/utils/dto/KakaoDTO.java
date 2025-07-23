package com.ppu.ppu.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class KakaoDTO {
    @Data
    public static class AuthorizeCode {
        private String code;
//        private String error;
//        private String error_description;
//        private String state;
    }


    @Data
    public static class OAuthToken {
//        private String token_type;
        private String access_token;
//        private String refresh_token;
//        private Integer expires_in;
//        private Integer refresh_token_expires_in;
    }

    @Data
    public static class UserProfile {
        private Long id;

        @JsonProperty("kakao_account")
        private KakaoAccount kakaoAccount;

        @Data
        public static class KakaoAccount {
            private String name;
            private String email;

            private String birthyear;
            private String birthday;
            private String birthday_type;

            private String gender;

            private Profile profile;

            @Data
            public static class Profile {
                private String nickname;
//                private String profile_image_url;
            }
        }
    }
}
