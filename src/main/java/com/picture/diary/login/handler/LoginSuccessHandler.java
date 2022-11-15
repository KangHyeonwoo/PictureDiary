package com.picture.diary.login.handler;

import com.picture.diary.common.jwt.JwtProvider;
import com.picture.diary.common.user.data.User;
import com.picture.diary.login.data.LoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpStatus.SC_OK);

        User loginUser = (User) authentication.getPrincipal();
        String userId = loginUser.getUserId();
        LoginType loginType = loginUser.getLoginType();

        String token = jwtProvider.createToken(loginType, userId, loginUser.getAuthorities());
        log.info("Login Success auth is : {} " , token);

        response.setHeader("auth", token);
        //메인화면으로 이동해야 하는데 URL 세팅을 여기서 하는게 맞는가..
        //클라이언트에서 하는게 맞는가..

    }
}
