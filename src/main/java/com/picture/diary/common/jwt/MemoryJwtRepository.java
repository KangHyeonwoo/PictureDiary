package com.picture.diary.common.jwt;

import java.util.HashMap;
import java.util.Map;

public class MemoryJwtRepository implements JwtRepository {

    private final Map<String, String> tokenMap = new HashMap<>();

    @Override
    public void save(JwtEntity jwtEntity) {
        tokenMap.put(jwtEntity.getRefreshToken(), jwtEntity.getAccessToken());
    }

    @Override
    public String findRefreshTokenByAccessToken(String accessToken) {
        if(tokenMap.containsValue(accessToken)) {
            return tokenMap.keySet().stream()
                    .filter(key -> tokenMap.get(key) == accessToken)
                    .findFirst().get();
        }

        return null;
    }

    @Override
    public void refreshAccessToken(JwtEntity jwtEntity) {
        tokenMap.put(jwtEntity.getRefreshToken(), jwtEntity.getAccessToken());
    }
}
