package com.picture.diary.common.exception;

public interface BaseExceptionType {

	int getErrorCode();
	int getHttpStatus();
	String getErrorMessage();
}
