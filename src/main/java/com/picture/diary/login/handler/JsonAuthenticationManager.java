package com.picture.diary.login.handler;

import com.picture.diary.common.exception.PictureDiaryException;
import com.picture.diary.common.jwt.JwtEntity;
import com.picture.diary.common.jwt.JwtRepository;
import com.picture.diary.common.response.ErrorResponse;
import com.picture.diary.common.user.service.UserService;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.data.LoginType;
import com.picture.diary.login.service.LoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;


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
        log.info("===== JWT 로 인증하는 절차 단계 =====");
        log.info("requestUsername : " + requestUsername);
        log.info("requestPassword : " + requestPassword);

        //로그인
        try {
            loginService.login(new LoginRequestDto(LoginType.SYNOLOGY_NAS, requestUsername, requestPassword));
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException("aaa");
        }

        //만약 실패 시 에러 리턴
        //TODO 위로 올리기
        if(StringUtils.isEmpty(requestUsername) || StringUtils.isEmpty(requestPassword)) {
            throw new AuthenticationServiceException("아이디 또는 비밀번호를 입력해주세요.");
        }

        //JWT 인증 성공 시 해당 사용자의 권한을 조회
        UserDetails userDetails = userService.loadUserByUsername(requestUsername);

        //인증이 완료되면
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        if(userDetails == null) {
            throw new AuthenticationServiceException("사용자 정보가 존재하지 않습니다.");
        }

        //TODO 뭐가 리턴되는지 확인해야함.
        return result;
    }

}
