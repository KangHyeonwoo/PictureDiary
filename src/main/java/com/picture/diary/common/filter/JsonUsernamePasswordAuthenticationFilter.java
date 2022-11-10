package com.picture.diary.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.common.provider.ApplicationContextProvider;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.handler.LoginSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * TODO :  로 클래스명 변경하기
 * @See : https://jeong-pro.tistory.com/205
 */
@Slf4j
@Component
@Deprecated
public class JsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    //private final MessageSourceAccessor messageSource;
    public static final String HTTP_METHOD = "POST";
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/login", HTTP_METHOD);

    public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager,
                                                    LoginSuccessHandler loginSuccessHandler, AuthenticationFailureHandler authenticationFailureHandler) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        log.info("여기 오냐?");
        this.objectMapper =objectMapper;
        setAuthenticationSuccessHandler(loginSuccessHandler);
        setAuthenticationFailureHandler(authenticationFailureHandler);

         //messageSource = ApplicationContextProvider.getApplicationContext().getBean(MessageSourceAccessor.class);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        //Request Method must be POST , and request content-type must be json.
        if (!request.getMethod().equals(HTTP_METHOD) || !request.getContentType().equals("application/json")) {

            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        LoginRequestDto loginDto = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), Charset.defaultCharset()), LoginRequestDto.class);


        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        if(username ==null || password == null){
            //String errorMessage = messageSource.getMessage("login.error.paramEmpty");
            throw new AuthenticationServiceException("아이디 또는 비밀번호를 입력해주세요.");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, UsernamePasswordAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
