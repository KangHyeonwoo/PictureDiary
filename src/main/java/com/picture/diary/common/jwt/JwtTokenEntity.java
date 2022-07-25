package com.picture.diary.common.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class JwtTokenEntity {

    private String userId;

    private String accessToken;

    private String refreshToken;

    public JwtToken toJwtToken() {
        return new JwtToken(accessToken, refreshToken);
    }
}
