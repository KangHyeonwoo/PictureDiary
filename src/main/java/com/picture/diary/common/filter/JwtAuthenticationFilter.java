package com.picture.diary.common.filter;

import com.picture.diary.common.jwt.JwtEntity;
import com.picture.diary.common.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        //TODO if depth 가 너무 많음 수정 필요
        if(accessToken != null) {
            JwtEntity validateResponseToken = jwtTokenProvider.getValidateResponseToken(accessToken);
            //유효성 검사에 성공한 토큰이면
            if(validateResponseToken.isValidate()) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if(validateResponseToken.isNeedRefresh()) {
                    jwtTokenProvider.setRefreshToken((HttpServletResponse) response, validateResponseToken);
                }
            }
        }

        chain.doFilter(request, response);
    }

}
