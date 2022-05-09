package com.picture.diary.login.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginRequestDto {

    private String userId;
    private String password;
}
