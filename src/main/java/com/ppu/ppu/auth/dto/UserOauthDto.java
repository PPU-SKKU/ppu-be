package com.ppu.ppu.auth.dto;

import com.ppu.ppu.user.domain.Gender;
import com.ppu.ppu.user.domain.LoginType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOauthDto {
    @NotBlank
    @Email
    @NotNull
    private String email;

    private String name;

    private String nickname;

    private LocalDate birth;

    private Gender gender;

    @NotNull
    private LoginType loginType;
}
