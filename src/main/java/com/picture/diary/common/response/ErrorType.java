package com.picture.diary.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {

    INCORRECT_INFORMATION(HttpStatus.UNAUTHORIZED, "올바르지 않은 정보입니다.");

    private HttpStatus httpStatus;
    private String message;
}
