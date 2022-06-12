package com.picture.diary.login.service;

import com.picture.diary.login.data.LoginRequestDto;

import java.io.IOException;

public interface LoginService {

    void login(LoginRequestDto loginRequestDto) throws IOException;

    void logout(LoginRequestDto loginRequestDto);
}
