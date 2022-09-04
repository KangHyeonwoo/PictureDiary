package com.picture.diary.login.controller;

import com.picture.diary.common.response.BasicResponse;
import com.picture.diary.common.response.SuccessResponse;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class LoginRestController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<BasicResponse> login(@Valid LoginRequestDto loginRequestDto) throws IOException {
        String accessToken = loginService.login(loginRequestDto);

        SuccessResponse<String> response = new SuccessResponse<>(accessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<BasicResponse> logout(@RequestParam String userId) {

        return ResponseEntity.ok(new SuccessResponse<>());
    }
}
