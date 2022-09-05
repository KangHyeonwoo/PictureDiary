package com.picture.diary.common.user.service;

import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final LoginService loginService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return null;
    }

    public String authenticateByUserNameAndPassword(String username, String password) throws IOException {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .userId(username)
                .password(password)
                .build();

        String jwt = loginService.login(loginRequestDto);

        return jwt;
    }
}
