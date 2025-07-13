package com.ppu.ppu.user.dto;

import com.ppu.ppu.user.Gender;
import com.ppu.ppu.user.LoginType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCreateDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private LocalDate birth;
    private Gender gender;
//    private LoginType loginType;

    public UserCreateDto(String email, String password, String name, String nickname, LocalDate birth, Gender gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
    }
}
