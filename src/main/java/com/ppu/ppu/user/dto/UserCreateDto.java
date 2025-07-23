package com.ppu.ppu.user.dto;

import com.ppu.ppu.user.Gender;
import com.ppu.ppu.user.LoginType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private LocalDate birth;
    private Gender gender;
    private LoginType loginType;
}
