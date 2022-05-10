package com.picture.diary.common.connection;

import com.picture.diary.common.connection.response.ConnectionResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/*
    응답 성공
     - 정상적으로 결과 리턴
     - 정상적으로 응답은 받았으나 잘못된 요청일 경우 (혹은 서버 오류이거나)
    응답 실패

    -> SUCCESS, FAIL, ERROR
 */

/**
 *  연결 응답 모델
 */
public class ConnectionResponse<T extends ConnectionResponseEntity> {

    private ResponseCode code;      // Response Status
    private T responseData;        // Response Data
    private String message;         // Error Message

    public boolean isSuccess() {

        return code == ResponseCode.SUCCESS;
    }

    public T getResponseData() {
        return responseData;
    }

    public String getMessage() {
        return this.message;
    }

    protected static <T extends ConnectionResponseEntity> ConnectionResponse<T> createSuccessInstance(String responseStr) {
        //responseData -> JSON 형식일 것이므로 우선 파싱
        //code 추출해서 성공일 경우 분기
        //만약에 성공이면 ResponseCode.SUCCESS ConnectionResponse 객체 리턴하고
        //그렇지 않으면 ResponseCode.FAIL ConnectionResponse 객체 리턴

        Map<String, Object> responseMap = new HashMap<>();


        //TODO response 의 자료형 고민하기... String 은 아닌 것 같아.
        return new ConnectionResponse(ResponseCode.SUCCESS, null, "");
    }

    protected static ConnectionResponse createFailInstance(String responseData) {

        return new ConnectionResponse(ResponseCode.FAIL, null, "");
    }

    protected static ConnectionResponse createErrorInstance(String errorMessage) {

        return new ConnectionResponse(ResponseCode.ERROR, null, errorMessage);
    }

    private ConnectionResponse(ResponseCode code, T t, String message) {
        this.code = code;
        this.message = message;
        this.responseData = t;
    }
}
