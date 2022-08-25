package com.picture.diary.login.service.impl;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.common.jwt.JwtTokenProvider;
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

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginRequestDto loginRequestDto) throws IOException {
        //1.  param setting
        final String userId = loginRequestDto.getUserId();
        final String password = loginRequestDto.getPassword();

        NasConnection connection = new NasConnection.create(NasConnectionType.LOGIN)
                .addParam("account", userId)
                .addParam("passwd", password)
                .send();

        if(connection.isSuccess()) {
            //TODO DB 에 사용자 ROLE 저장 조회
            //현재는 임시 데이터 세팅
            //return new LoginResponseEntity(LoginType.SYNOLOGY_NAS, userId, LocalDateTime.now(), Arrays.asList("USER"));
            return jwtTokenProvider.createToken(LoginType.SYNOLOGY_NAS, userId, Arrays.asList(""));
        }

        throw new PictureDiaryException(connection.createErrorResponse());
    }

    //로그아웃 처리는 컨트롤러에서 해도 괜찮지 않을까?
    //session remove 말고 다른 기능이 필요할까?
    @Override
    public void logout(String userId) {
    }

}
