package com.picture.diary.common.exception;

public interface BaseExceptionType {

	String getErrorCode();

	int getHttpStatus();

	String getErrorMessage();
}
