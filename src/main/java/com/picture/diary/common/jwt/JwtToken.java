package com.picture.diary.common.jwt;

import io.jsonwebtoken.JwtException;
import lombok.Getter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Getter
public class JwtToken {

    private String accessToken;

    private String refreshToken;

    public JwtToken(HttpServletRequest request) throws JwtException {
        String accessToken = request.getHeader("Access-Token");
        String refreshToken = request.getHeader("Refresh-Token");

        if(StringUtils.isEmpty(accessToken)) {
            throw new JwtException("Access-Token must be not empty");
        }

        if(StringUtils.isEmpty(refreshToken)) {
            throw new JwtException("Refresh-Token must be not empty");
        }

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
