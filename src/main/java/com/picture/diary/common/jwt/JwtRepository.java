package com.picture.diary.common.jwt;

//우선은 메모리로 테스트 후에 DB로 조회
public interface JwtRepository {

    void save(JwtEntity jwtEntity);

    String findRefreshTokenByAccessToken(String refreshToken);

    void refreshAccessToken(JwtEntity jwtToken);

}
