package com.picture.diary.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.common.exception.PictureDiaryException;
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
import org.springframework.util.StringUtils;

import static org.springframework.http.HttpMethod.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class NasConnection {

    private final String baseUrl = "https://hwkang.synology.me:5001/webapi/entry.cgi";

    private final CloseableHttpClient httpClient = HttpClients.createDefault();
    private final NasConnectionType connectionType;

    //사용자 요청 파라미터 맵객체
    private final Map<String, String> paramMap;

    //Http request & response
    private HttpUriRequest httpRequest;
    private CloseableHttpResponse httpResponse;

    //성공 여부
    private boolean isSuccess = false;

    //Json 형태의 응답 문자열
    private String response;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private NasConnection(NasConnectionType connectionType, Map<String, String> paramMap) throws IOException {
        log.info("Request NAS API : " + connectionType.name());
        //JSON 중 내가 필드로 선언한 데이터들만 파싱하겠다는 설정
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.connectionType = connectionType;
        this.paramMap = paramMap;

        String queryString = this.getQueryString();
        log.info("==> Create Query String. [ " + queryString + " ]");

        HttpMethod requestMethod = connectionType.getHttpMethod();
        if(requestMethod == GET) {
            httpRequest = new HttpGet(baseUrl + queryString);
        }

        //TODO -> POST 일 경우

        this.httpResponse = httpClient.execute(httpRequest);
        this.response = getResponse();
        this.isSuccess = !response.contains("error");

        log.info("==> Is success : " + isSuccess);
        log.info("==> Response : " + this.response);
    }

    /**
     * API 응답 객체를 조회한다.
     *
     * @return Json 형태의 String
     * @throws IOException
     */
    private String getResponse() throws IOException {
        StringBuffer responseBuffer = new StringBuffer();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
            String inputLine;

            while((inputLine = reader.readLine()) != null) {
                responseBuffer.append(inputLine);
            }
        }

        return responseBuffer.toString();
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    /**
     * 응답 객체 조회
     * @return Map
     */
    public Map<String, Object> getResponseMap() {
        //TODO "{"data":{"did":"NsxKiXN3LKcFG9VrN-lyDp72EMd9HJAvIZDLyUzE1JNv_SRsUfs_nC1wyV9JJYRu7p7Nn_gQNodf_8C8QNrPHw","is_portal_port":false,"sid":"uIUH_qNPoZ3QCugEyUApT7Ex6gWPE_WY0aR3f5_OQ3ENJQDDyx43keVsJG2Har-i2iS2ZjHCM3zq-sRKz7uxjY"},"success":true}"
        //여기서 data 부분만 파싱해야 함.
        try {
            Map<String, Object> responseMap = objectMapper.readValue(this.response, HashMap.class);
            Map<String, Object> dataMap =  (Map<String, Object>) responseMap.get("data");

            return dataMap;
        } catch (JsonProcessingException e) {
            log.error(" ===> response parse error.");
            log.error("  ==> origin response : [ " + this.response + " ] ");

            throw new PictureDiaryException("response parse error");
        }
    }

    /**
     * 에러 객체 생성
     *  - Synology API 의 응답 결과를 바탕으로 에러 객체를 리턴한다.
     * @return
     * @throws IOException
     */
    public ErrorResponse createErrorResponse() throws IOException {
        if(isSuccess) {
            throw new RuntimeException("성공한 객체임");
        }
        NasConnectionErrorResponse errorResponse = objectMapper.readValue(response, NasConnectionErrorResponse.class);

        return errorResponse.toErrorResponse();
    }

    /**
     * QueryString 생성
     *  - NasConnectionType 에 정의된 Default Parameters Map 과 사용자가 입력한 파라미터를 쿼리스트링으로 만들어 리턴한다.
     *
     * @return
     * @throws PictureDiaryException 사용자가 추가한 파라미터에 NasConnectionType 에 정의한 필수 파라미터가 없는 경우 예외 발생
     */
    private String getQueryString() throws PictureDiaryException {
        //required parameter empty check
        String reqParam = this.getRequiredParameterNotContains();

        if(StringUtils.hasLength(reqParam)){
            String errorMessage = reqParam + " is must be contains";
            throw new PictureDiaryException(new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage));
        }

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

    private String getRequiredParameterNotContains() {
        String[] requiredParams = this.connectionType.getRequiredParams();

        for(String requiredParam : requiredParams) {
            if(!paramMap.containsKey(requiredParam)) {
                return requiredParam;
            }
        }

        return "";
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