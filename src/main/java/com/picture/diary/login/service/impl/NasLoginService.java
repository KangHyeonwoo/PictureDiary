package com.picture.diary.login.service.impl;

import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.service.LoginService;
import com.picture.diary.picture.file.data.NasProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NasLoginService implements LoginService {

    private final NasProperty nasProperty;

    @Override
    public void login(LoginRequestDto loginRequestDto) {
        String userId = loginRequestDto.getUserId();
        String password = loginRequestDto.getPassword();

    }

    @Override
    public void logout(LoginRequestDto loginRequestDto) {

    }
}
