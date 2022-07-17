package com.picture.diary.login.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.common.response.BasicResponse;
import com.picture.diary.common.response.ErrorResponse;
import com.picture.diary.common.response.SuccessResponse;
import com.picture.diary.common.session.SessionConstants;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.data.LoginResponseEntity;
import com.picture.diary.login.service.LoginService;
import com.picture.diary.utils.NasProperty;
import com.picture.diary.utils.NasConnection;
import com.picture.diary.utils.NasConnectionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasLoginService implements LoginService {

    @Override
    public LoginResponseEntity login(LoginRequestDto loginRequestDto) throws IOException {
        //1.  param setting
        final String userId = loginRequestDto.getUserId();
        final String password = loginRequestDto.getPassword();

        NasConnection connection = new NasConnection.create(NasConnectionType.LOGIN)
                .addParam("account", userId)
                .addParam("passwd", password)
                .send();

        if(connection.isSuccess()) {
            return LoginResponseEntity.of(connection.getResponseMap());
        }

        throw new PictureDiaryException(connection.createErrorResponse());
    }

    //로그아웃 처리는 컨트롤러에서 해도 괜찮지 않을까?
    //session remove 말고 다른 기능이 필요할까?
    @Override
    public void logout(String userId) {
    }

    private boolean failedLogin(String responseStr) {
        return responseStr.contains("error");
    }
}
