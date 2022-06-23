package com.picture.diary.utils;

import com.picture.diary.common.response.BasicResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpMethod;
import static org.springframework.http.HttpMethod.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NasConnection {

    private String baseUrl = "https://hwkang.synology.me:5001/webapi/entry.cgi";

    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private HttpRequest httpRequest;
    private NasConnectionType connectionType;
    private Map<String, String> paramMap;
    private String response = "";

    private NasConnection(NasConnectionType connectionType, Map<String, String> paramMap) {
        this.connectionType = connectionType;
        this.paramMap = paramMap;

        String queryString = this.getQueryString();
        log.info("==> Create Query String. [ " + queryString + " ]");

        HttpMethod requestMethod = connectionType.getHttpMethod();
        if(requestMethod == GET) {
            HttpGet httpGet = new HttpGet(baseUrl + queryString);
        }


    }

    private String getQueryString() {
        final Map<String, String> defaultParamMap = this.connectionType.getDefaultParamMap();
        final StringBuffer queryString = new StringBuffer("?");

        paramMap.keySet().forEach(key ->
                queryString.append(key).append("=").append(paramMap.get(key)).append("&")
        );
        defaultParamMap.keySet().forEach(key ->
                queryString.append(key).append("=").append(defaultParamMap.get(key)).append("&")
        );

        return queryString.toString();
    }

    public BasicResponse getResponse() {

        return null;
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