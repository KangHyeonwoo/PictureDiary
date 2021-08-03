package com.picture.diary.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.picture.diary.response.ErrorResponse;

/**
 * 예외처리 핸들러
 * 참초 URL : https://velog.io/@hanblueblue/Spring-ExceptionHandler
 */
@ControllerAdvice
public class PictureDiaryExceptionHandler {

	@ResponseBody
	@ExceptionHandler(PictureDiaryException.class)
	public ResponseEntity<ErrorResponse> commonExceptionHandler(PictureDiaryException exception) {
		BaseExceptionType exceptionType = exception.getExceptionType();
		
		return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.OK);
	}
	
	//https://www.baeldung.com/spring-boot-bean-validation
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validationExceptionHandler(MethodArgumentNotValidException me) {
		
		me.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			String errorCode = error.getCode();
			System.out.println("fieldName : " + fieldName + " , errorMessage : " + errorMessage + " , errorCode : " + errorCode);
		});
		
		return null;
	}
}
