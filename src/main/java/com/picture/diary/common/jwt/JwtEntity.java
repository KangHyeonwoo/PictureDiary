package com.picture.diary.common.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class JwtEntity {

    private String accessToken;

    private String refreshToken;

    private boolean needRefresh = false;

    private boolean validate = false;

    //JWT 에 들어가는 사용자 정보
    private String userPk;

    private List<String> roles;

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
