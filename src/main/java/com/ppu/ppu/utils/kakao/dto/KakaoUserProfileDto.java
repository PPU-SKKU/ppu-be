package com.ppu.ppu.utils.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoUserProfileDto {
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
//            private String profile_image_url;
        }
    }
}
