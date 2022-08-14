package com.picture.diary.common.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class JwtEntity {

    private String requestToken;

    private String accessToken;

    private String refreshToken;

    private boolean needRefresh;

    private boolean validate;

    //JWT 에 들어가는 사용자 정보
    private String userPk;

    private List<String> roles;

    public JwtEntity(String requestToken) {
        this.validate = false;
    }

    public JwtEntity(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }
}
