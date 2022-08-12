package com.picture.diary.common.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtEntity {

    private String requestToken;

    private String accessToken;

    private String refreshToken;

    private boolean needRefresh;

    private boolean validate;

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
