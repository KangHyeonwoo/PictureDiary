package com.picture.diary.common.connection.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picture.diary.common.connection.ResponseCode;

import java.util.Map;

//https://www.baeldung.com/spring-webclient-json-list
public class ConnectionResponse {

    private final ResponseCode responseCode;
    private final String responseData;

    public ConnectionResponse(int responseCode, String responseStr) {

        if(responseCode >= 200 && responseCode < 300) {
            this.responseCode = ResponseCode.SUCCESS;
        } else {
            this.responseCode = ResponseCode.FAIL;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        this.responseData = responseStr;
    }

    public boolean isSuccess() {
        return this.responseCode == ResponseCode.SUCCESS;
    }

}
