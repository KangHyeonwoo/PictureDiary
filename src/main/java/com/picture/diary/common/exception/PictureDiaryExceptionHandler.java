package com.picture.diary.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.picture.diary.response.ErrorResponse;

/**
 * 예외처리 핸들러
 * 참초 URL : https://velog.io/@hanblueblue/Spring-ExceptionHandler
 */
@ControllerAdvice
public class PictureDiaryExceptionHandler {

	@ResponseBody
	@ExceptionHandler(PictureDiaryException.class)
	public ResponseEntity<ErrorResponse> exception(PictureDiaryException exception) {
		
		return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.OK);
	}
}
