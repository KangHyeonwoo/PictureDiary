package com.picture.diary.auth.login.handler;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.common.user.service.UserService;
import com.picture.diary.auth.login.data.LoginRequestDto;
import com.picture.diary.auth.login.data.LoginType;
import com.picture.diary.auth.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 *  이 클래스는 로그인일 때만 들어옴
 *  그래서 authenticate 메소드는 jwt 토큰이 없다고 가정하고 진행해야 함
 *  @See : https://velog.io/@jkijki12/Spirng-Security-Jwt-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class JsonAuthenticationManager implements AuthenticationManager {

    private final LoginService loginService;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String requestUsername = authentication.getName();
        String requestPassword = authentication.getCredentials().toString();

        //JWT 로 인증하는 절차 필요
        log.info("===== JWT Authentication Start. =====");
        log.info(" ==> requestUsername : " + requestUsername);

        //로그인
        try {
            loginService.login(new LoginRequestDto(LoginType.SYNOLOGY_NAS, requestUsername, requestPassword));
        } catch (PictureDiaryException e) {
            throw new AuthenticationServiceException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException("기타 오류");
        }

        //JWT 인증 성공 시 해당 사용자의 권한을 조회
        UserDetails userDetails = userService.loadUserByUsername(requestUsername);
        if(userDetails == null) {
            throw new AuthenticationServiceException("사용자 정보가 존재하지 않습니다.");
        }

        //인증이 완료되면
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
