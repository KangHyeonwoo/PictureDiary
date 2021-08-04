package com.picture.diary.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.picture.diary.response.ErrorResponse;

/**
 * 예외처리 핸들러
 * 참초 URL : https://velog.io/@hanblueblue/Spring-ExceptionHandler
 * 			 https://www.baeldung.com/spring-boot-bean-validation
 */
@ControllerAdvice
public class PictureDiaryExceptionHandler {

	@ResponseBody
	@ExceptionHandler(PictureDiaryException.class)
	public ResponseEntity<ErrorResponse> commonExceptionHandler(PictureDiaryException exception) {
		BaseExceptionType exceptionType = exception.getExceptionType();
		
		final String errorCode = exceptionType.getErrorCode();
		final int status = exceptionType.getHttpStatus();
		final String errorMessage = exceptionType.getErrorMessage();
		
		HttpStatus httpStatus = HttpStatus.valueOf(status);
		
		return new ResponseEntity<>(new ErrorResponse(errorCode, status, errorMessage), httpStatus);
	}
	
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> validationExceptionHandler(MethodArgumentNotValidException me) {
		for(int i=0; i<me.getBindingResult().getAllErrors().size();) {
			ObjectError error = me.getBindingResult().getAllErrors().get(i);
			
			final String errorCode = error.getCode();
			final int status = 400;
			final String errorMessage = error.getDefaultMessage();
			
			return new ResponseEntity<>(new ErrorResponse(errorCode, status, errorMessage), HttpStatus.BAD_REQUEST);
		}
		
		return null;
	}
}
