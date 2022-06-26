package com.picture.diary.login.service;

import com.picture.diary.common.response.BasicResponse;
import com.picture.diary.login.data.LoginRequestDto;

import java.io.IOException;

public interface LoginService {

    /**
     * 로그인
     *
     * @param loginRequestDto
     * @return
     * @throws IOException
     */
    BasicResponse login(LoginRequestDto loginRequestDto) throws IOException;

    /**
     * 로그아웃
     *
     * @param userId
     */
    void logout(String userId);
}
