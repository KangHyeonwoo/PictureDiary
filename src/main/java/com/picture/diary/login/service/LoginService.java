package com.picture.diary.login.service;

import com.picture.diary.login.data.LoginRequestDto;

public interface LoginService {

    void login(LoginRequestDto loginRequestDto);

    void logout(LoginRequestDto loginRequestDto);
}
