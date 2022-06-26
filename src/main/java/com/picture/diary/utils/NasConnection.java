package com.picture.diary.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.common.response.BasicResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpMethod.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NasConnection {

    private final String baseUrl = "https://hwkang.synology.me:5001/webapi/entry.cgi";

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final NasConnectionType connectionType;
    private final Map<String, String> paramMap;

    private HttpUriRequest httpRequest;
    private CloseableHttpResponse httpResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private NasConnection(NasConnectionType connectionType, Map<String, String> paramMap) throws IOException {
        this.connectionType = connectionType;
        this.paramMap = paramMap;

        String queryString = this.getQueryString();
        log.info("==> Create Query String. [ " + queryString + " ]");

        HttpMethod requestMethod = connectionType.getHttpMethod();
        if(requestMethod == GET) {
            httpRequest = new HttpGet(baseUrl + queryString);
        }

        httpResponse = httpClient.execute(httpRequest);
    }

    public String getResponse() throws IOException {
        StringBuffer response = new StringBuffer();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
            String inputLine;

            while((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
        }
        Map<String, Object> responseMap = objectMapper.readValue(response.toString(), HashMap.class);
        System.out.println("responseMap : ");
        System.out.println(responseMap.toString());

        if(responseMap.containsKey("error")) {
            responseMap.keySet().forEach(key -> {
                System.out.println("key : " + key);
                System.out.println("value : " + responseMap.get(key));
            });

            String error = responseMap.get("error").toString();

            Map<String, Object> errorMap = objectMapper.readValue(error, HashMap.class);
            errorMap.keySet().forEach(key -> {
                System.out.println("error key : " + key);
                System.out.println("error value : " + errorMap.get(key));
            });
        }

        return response.toString();
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

    private void validation() {
        String[] reqParams = this.connectionType.getRequiredParams();
        for(String reqParam : reqParams) {
            this.paramMap.keySet().stream().findFirst();
        }
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

        public NasConnection send() throws IOException {
            return new NasConnection(connectionType, paramMap);
        }
    }
}