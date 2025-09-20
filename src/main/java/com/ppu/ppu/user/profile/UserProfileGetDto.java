package com.ppu.ppu.user.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileGetDto {
    private URL profileImage;
    private String nickname;
}
