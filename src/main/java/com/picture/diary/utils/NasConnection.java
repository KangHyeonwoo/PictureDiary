package com.picture.diary.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.common.response.BasicResponse;
import com.picture.diary.common.response.ErrorResponse;
import com.picture.diary.common.response.SuccessResponse;
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

        //JSON 중 내가 필드로 선언한 데이터들만 파싱하겠다는 설정
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public String getResponse() throws IOException {
        StringBuffer responseBuffer = new StringBuffer();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
            String inputLine;

            while((inputLine = reader.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
        }

        return responseBuffer.toString();
    }

    /**
     * Return of Nas Connection Response Information.
     *
     * @return BasicResponse 를 확장한 SuccessResponse , ErrorResponse 객체
     * @throws IOException
     */
    public BasicResponse getBasicResponse() throws IOException {
        String response = this.getResponse();

        if(response.contains("error")) {
            NasConnectionErrorResponse errorResponse = objectMapper.readValue(response, NasConnectionErrorResponse.class);

            return errorResponse.toErrorResponse();
        }

        return new SuccessResponse<>();
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

    private ErrorResponse createErrorResponse() {

        return null;
    }

    private SuccessResponse createSuccessResponse() {

        return null;
    }

    /*
    private void validation() {
        String[] reqParams = this.connectionType.getRequiredParams();
        for(String reqParam : reqParams) {
            this.paramMap.keySet().stream().findFirst();
        }
    }
    */
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