package com.picture.diary.login.handler;

import com.picture.diary.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonAuthenticationManager implements AuthenticationManager {

    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("JsonAuthenticationManager 이 동작합니다!!!");
        UserDetails userDetails = userService.loadUserByUsername((String) authentication.getPrincipal());//username

        return new UsernamePasswordAuthenticationToken(userDetails
                , userDetails.getPassword()
                , userDetails.getAuthorities());
    }
}
