package com.picture.diary.login.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.login.data.LoginRequestDto;
import com.picture.diary.login.data.LoginResponseEntity;
import com.picture.diary.login.service.LoginService;
import com.picture.diary.picture.file.data.NasProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NasLoginService implements LoginService {

    private final NasProperty nasProperty;
    //private final WebClient webClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void login(LoginRequestDto loginRequestDto) {
        final String userId = loginRequestDto.getUserId();
        final String password = loginRequestDto.getPassword();
        String apiPath = nasProperty.getApiPath();
/*
        //TODO study 에 정리할 때 예제소스코드로 작성 후 삭제하기
        LoginResponseEntity responseEntity = webClient
                .get()
                .uri(apiPath + "/userId={userId}&password={password}", userId, password)
                .retrieve()
                .onStatus(
                        status -> !status.is2xxSuccessful(),
                        r -> Mono.empty()
                )
                .bodyToMono(LoginResponseEntity.class)
                .block();
*/

/*
        Mono<Object> loginResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(apiPath)
                        .queryParam("api", "SYNO.API.Auth")
                        .queryParam("version", "6")
                        .queryParam("method", "login")
                        .queryParam("session", "FileStation")
                        .queryParam("format", "cookie")
                        .queryParam("account", userId)
                        .queryParam("password", password)
                        .build())
                .retrieve()
                .onStatus(
                        httpStatus -> !httpStatus.is2xxSuccessful(),
                        r -> Mono.empty()
                )
                .bodyToMono(String.class)
                .flatMap(responseObj -> {

                });
*/


        objectMapper.convertValue(new Object(), LoginResponseEntity.class);

    }

    @Override
    public void logout(LoginRequestDto loginRequestDto) {

    }
}
