package com.picture.diary.common.jwt;

//우선은 메모리로 테스트 후에 DB로 조회
public interface JwtRepository {

    void save(JwtTokenEntity jwtTokenEntity);

    JwtTokenEntity findByUserId(String userId);

    JwtTokenEntity refreshAccessToken(String userId, String accessToken);
}
