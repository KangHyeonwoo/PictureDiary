package com.picture.diary.extract.exception;


public class PictureExtractException extends RuntimeException {
	private static final long serialVersionUID = -3283267995844122149L;
	
	public PictureExtractException(PictureExtractExceptionTypes errorType) {
		super(errorType.getMessage());
	}
	
	public PictureExtractException() {
		super(PictureExtractExceptionTypes.ETC_EXCEPTION.getMessage());
	}
}
