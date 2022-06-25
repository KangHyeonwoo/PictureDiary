package com.picture.diary.login.data;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class LoginRequestDto {

    @NotEmpty(message = "ID를 입력해주세요.")
    @Size(min = 4, max = 20, message = "사용자 ID는 4 ~ 20 글자 사이로 입력이 가능합니다.")
    private String userId;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "사용자 비밀번호는 8 ~ 20 글자 사이로 입력이 가능합니다.")
    private String password;

    @Builder
    public LoginRequestDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
