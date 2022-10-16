package com.picture.diary.login.service.impl;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.data.LoginType;
import com.picture.diary.login.service.LoginService;
import com.picture.diary.utils.NasConnection;
import com.picture.diary.utils.NasConnectionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class NasLoginService implements LoginService {

    //private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    public String login(LoginRequestDto loginRequestDto) throws IOException {
        //1.  param setting
        final String userId = loginRequestDto.getUsername();
        final String password = loginRequestDto.getPassword();

        NasConnection connection = new NasConnection.create(NasConnectionType.LOGIN)
                .addParam("account", userId)
                .addParam("passwd", password)
                .send();

        if(connection.isSuccess()) {
            //TODO DB 에 사용자 ROLE 저장 조회
            //현재는 임시 데이터 세팅
            //return new LoginResponseEntity(LoginType.SYNOLOGY_NAS, userId, LocalDateTime.now(), Arrays.asList("USER"));
            //return jwtAuthenticationProvider.createToken(LoginType.SYNOLOGY_NAS, userId, Arrays.asList(""));
            return "fffff";
        }
        System.out.println("하이하이 여기오나욥");
        throw new PictureDiaryException(connection.createErrorResponse());
    }

}
