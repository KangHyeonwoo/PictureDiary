package com.picture.diary.common.connection.response;

import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private HttpStatus status;
    private String errorMessage;

    public ErrorResponse(int errorCode) {
        this.status = HttpStatus.resolve(errorCode);
    }
}
