package com.picture.diary.utils;

import org.apache.http.HttpRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class NasConnection {

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private HttpRequest httpRequest;
    private NasConnectionType connectionType;
    private Map<String, String> paramMap = new HashMap<>();
    private String response = "";

    private NasConnection(NasConnectionType connectionType, Map<String, String> paramMap) {
        this.connectionType = connectionType;
        this.paramMap = paramMap;

        String queryString = this.getQueryString();
    }

    private String getQueryString() {
        Map<String, String> defaultParamMap = this.connectionType.getDefaultParamMap();
        StringBuffer queryString = new StringBuffer("?");

        paramMap.keySet().forEach(key ->
                queryString.append(key).append("=").append(paramMap.get(key))
        );
        defaultParamMap.keySet().forEach(key ->
                queryString.append(key).append("=").append(defaultParamMap.get(key))
        );

        return queryString.toString();
    }

    public static class create {
        private NasConnectionType connectionType;
        private final Map<String, String> paramMap = new HashMap<>();

        public create(NasConnectionType connectionType) {
            this.connectionType = connectionType;
        }

        public create addParam(String key, String value) {
            paramMap.put(key, value);

            return this;
        }

        public NasConnection send() {
            return new NasConnection(connectionType, paramMap);
        }
    }
}
