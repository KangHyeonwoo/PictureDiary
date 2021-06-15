package com.picture.diary.extract.exception;

public class NotFoundDateException extends RuntimeException{

	private static final long serialVersionUID = 193056430175009903L;
	
	public NotFoundDateException() {
		super();
	}
	
	public NotFoundDateException(String message) {
		super(message);
	}
}
