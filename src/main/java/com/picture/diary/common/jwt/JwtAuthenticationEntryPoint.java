package com.picture.diary.common.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error("Responding with unauthorized error. Message - {}", e.getMessage());

        String unAuthorizationCode = (String) httpServletRequest.getAttribute("unauthorization.code");

        httpServletRequest.setAttribute("response.failure.code", unAuthorizationCode);
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "에러메시지");
    }
}
