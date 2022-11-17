package com.picture.diary.auth.login.service.impl;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.auth.login.data.LoginRequestDto;
import com.picture.diary.auth.login.service.LoginService;
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

    @Override
    public void login(LoginRequestDto loginRequestDto) throws IOException {
        //1.  param setting
        final String userId = loginRequestDto.getUsername();
        final String password = loginRequestDto.getPassword();

        NasConnection connection = new NasConnection.create(NasConnectionType.LOGIN)
                .addParam("account", userId)
                .addParam("passwd", password)
                .send();

        if(!connection.isSuccess()) {
            throw new PictureDiaryException(connection.createErrorResponse());
        }
    }

}
