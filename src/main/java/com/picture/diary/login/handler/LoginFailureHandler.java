package com.picture.diary.login.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("login fail");
        response.setStatus(HttpStatus.SC_UNAUTHORIZED);
        //TODO 이부분 문자열 말고 객체 -> 문자열 로 바꿔서 리턴하고 싶어..

        response.getWriter().print("{\"isSuccess\":false, \"message\":"+exception.getMessage()+"}");
        response.getWriter().flush();
    }
}
