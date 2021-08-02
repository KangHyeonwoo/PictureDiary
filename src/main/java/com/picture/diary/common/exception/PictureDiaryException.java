package com.picture.diary.common.exception;

import lombok.Getter;

public class PictureDiaryException extends RuntimeException {

	private static final long serialVersionUID = -3038145596406246261L;
	
	@Getter
	private BaseExceptionType exceptionType;
	
	public PictureDiaryException(BaseExceptionType exceptionType) {
		super(exceptionType.getErrorMessage());
		this.exceptionType = exceptionType;
	}
	
	public PictureDiaryException(String message) {
		super(message);
	}
}
