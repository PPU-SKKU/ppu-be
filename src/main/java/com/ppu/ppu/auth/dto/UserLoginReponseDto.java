package com.ppu.ppu.auth.dto;

public class UserLoginReponseDto {
    private String accessToken;

    public UserLoginReponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
