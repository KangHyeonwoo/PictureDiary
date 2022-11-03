package com.picture.diary.login.service;

import com.picture.diary.common.response.BasicResponse;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.data.LoginResponseEntity;

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
