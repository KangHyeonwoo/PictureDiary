package com.picture.diary.common.response;

import com.picture.diary.common.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends BasicResponse implements BaseExceptionType {

	private String errorCode;
	private int status;
	private String message;

	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public int getHttpStatus() {
		return this.status;
	}

	@Override
	public String getErrorMessage() {
		return this.message;
	}
}
