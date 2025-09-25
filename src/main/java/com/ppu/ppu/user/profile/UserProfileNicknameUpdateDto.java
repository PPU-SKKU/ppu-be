package com.ppu.ppu.user.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileNicknameUpdateDto {
    private String nickname;
}
