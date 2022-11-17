package com.picture.diary.auth.login.data;

import com.picture.diary.common.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class LoginResponseEntity extends SuccessResponse {

    //로그인 타입
    private LoginType loginType;

    //사용자 ID
    private String userId;

    private List<String> roles;

}
