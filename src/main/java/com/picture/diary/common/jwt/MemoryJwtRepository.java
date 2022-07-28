package com.picture.diary.common.jwt;

import java.util.HashMap;
import java.util.Map;

public class MemoryJwtRepository implements JwtRepository {

    private final Map<String, JwtEntity> tokenMap = new HashMap<>();

    @Override
    public void save(JwtEntity jwtEntity) {
        String userId = jwtEntity.getUserId();
        tokenMap.put(userId, jwtEntity);
    }

    @Override
    public JwtEntity findByUserId(String userId) {

        return tokenMap.get(userId);
    }

    @Override
    public void refreshAccessToken(String userId, JwtEntity jwtToken) {
        tokenMap.put(userId, jwtToken);
    }
}
