package com.picture.diary.common.jwt;

import java.util.HashMap;
import java.util.Map;

public class MemoryJwtRepository implements JwtRepository {

    private final Map<String, JwtToken> tokenMap = new HashMap<>();

    @Override
    public void save(JwtTokenEntity jwtTokenEntity) {
        String userId = jwtTokenEntity.getUserId();
        tokenMap.put(userId, jwtTokenEntity.toJwtToken());
    }

    @Override
    public JwtToken findByUserId(String userId) {

        return tokenMap.get(userId);
    }

    @Override
    public JwtTokenEntity refreshAccessToken(String userId, String accessToken) {
        return null;
    }
}
