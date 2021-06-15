package com.picture.diary.extract.exception;

public class NotFoundGeometryException extends RuntimeException {

	private static final long serialVersionUID = -1423832068753859637L;
	
	public NotFoundGeometryException() {
		super();
	}
	
	public NotFoundGeometryException(String message) {
		super(message);
	}
}
