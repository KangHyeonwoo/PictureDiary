package com.picture.diary.login.data;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class LoginRequestDto {

    @NotNull(message = "로그인 형식을 선택해주세요.")
    private LoginType loginType;

    @NotEmpty(message = "ID를 입력해주세요.")
    @Size(min = 4, max = 20, message = "사용자 ID는 4 ~ 20 글자 사이로 입력이 가능합니다.")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "사용자 비밀번호는 8 ~ 20 글자 사이로 입력이 가능합니다.")
    private String password;

    @Builder
    public LoginRequestDto(LoginType loginType, String username, String password) {
        this.loginType = loginType;
        this.username = username;
        this.password = password;
    }
}
