package com.picture.diary.common.connection;

import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.List;
import java.util.Map;

@Getter
public class ConnectionInfo {
    // PUSH
    @Builder.Default
    private String httpMethod = "GET";
    @Builder.Default
    private String protocol = "https";
    private String host;
    private String port;
    private String code;
    private String url; // 통합 인자값
    private List<JSONObject> dataList; // 리스트로된 json을 파라미터로 보낼경우
    private Map<String, Object> dataMap;

    private String filePath = "";

    @Builder
    public ConnectionInfo(String httpMethod, String protocol, String host, String port,
                          String code, String url, List<JSONObject> dataList, Map<String, Object> dataMap) {
        this.httpMethod = httpMethod;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.code = code;
        this.url = url;
        this.dataList = dataList;
        this.dataMap = dataMap;
    }
}
