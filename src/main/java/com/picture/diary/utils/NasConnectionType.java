package com.picture.diary.utils;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum NasConnectionType {
    LOGIN(
            HttpMethod.GET,
            new String[]{"account", "password"},
            new HashMap<>(){{
                put("api", "SYNO.API.Auth");
                put("version","6");
                put("method","login");
                put("session","FileStation");
                put("format", "cookie");
            }}
    );

    private HttpMethod httpMethod;
    private String[] requiredParams;
    private Map<String, String> defaultParamMap;

    NasConnectionType(HttpMethod httpMethod, String[] requiredParams, Map<String, String> defaultParamMap) {
        this.httpMethod = httpMethod;
        this.requiredParams = requiredParams;
        this.defaultParamMap = defaultParamMap;
    }
}
