package com.picture.diary.auth.login.service;

import com.picture.diary.auth.login.data.LoginRequestDto;

import java.io.IOException;

public interface LoginService {

    /**
     * 로그인
     *
     * @param loginRequestDto
     * @return JWT
     * @throws IOException
     */
    void login(LoginRequestDto loginRequestDto) throws IOException;

}
