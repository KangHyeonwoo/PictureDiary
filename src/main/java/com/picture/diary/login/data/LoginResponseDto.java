package com.picture.diary.login.data;

import lombok.Getter;

@Getter
public class LoginResponseDto {
    private final String mainPage;

    public LoginResponseDto(String mainPage) {
        this.mainPage = mainPage;
    }
}
