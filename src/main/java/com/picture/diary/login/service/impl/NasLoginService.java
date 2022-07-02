package com.picture.diary.login.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.common.response.BasicResponse;
import com.picture.diary.common.response.ErrorResponse;
import com.picture.diary.common.response.SuccessResponse;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.data.LoginResponseEntity;
import com.picture.diary.login.service.LoginService;
import com.picture.diary.utils.NasProperty;
import com.picture.diary.utils.NasConnection;
import com.picture.diary.utils.NasConnectionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasLoginService implements LoginService {

    private final NasProperty nasProperty;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public BasicResponse login(LoginRequestDto loginRequestDto) throws IOException {
        //1.  param setting
        final String userId = loginRequestDto.getUserId();
        final String password = loginRequestDto.getPassword();

        NasConnection connection = new NasConnection.create(NasConnectionType.LOGIN)
                .addParam("userId", userId)
                .addParam("password", password)
                .send();

        return connection.getBasicResponse(LoginResponseEntity.class);
    }

    @Override
    public void logout(String userId) {

    }

    private boolean failedLogin(String responseStr) {
        return responseStr.contains("error");
    }
}
