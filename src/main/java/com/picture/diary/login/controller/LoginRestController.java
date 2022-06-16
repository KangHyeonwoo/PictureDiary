package com.picture.diary.login.controller;

import com.picture.diary.common.response.BasicResponse;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class LoginRestController {

    private final LoginService loginService;

    @PostMapping("/login")
    public BasicResponse login(@Valid LoginRequestDto loginRequestDto) throws IOException {

        return loginService.login(loginRequestDto);
    }

    @PostMapping("/logout")
    public BasicResponse logout() {

        return null;
    }
}
