package com.ppu.ppu.user.dto;

import com.ppu.ppu.user.Gender;
import com.ppu.ppu.user.LoginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @NotBlank
    @Email
    @NotNull
    private String email;

    @NotBlank
    @NotNull
//    @Size(min = 8, max = 64, message = "too short or long password")
    private String password;

    private String name;

    @NotBlank
    @NotNull
    private String nickname;

    private LocalDate birth;

    private Gender gender;

    @NotNull
    private LoginType loginType;
}
