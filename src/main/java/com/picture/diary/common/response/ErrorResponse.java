package com.picture.diary.common.response;

import com.picture.diary.common.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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

	public ErrorResponse(HttpStatus status, String message) {
		this.status = status.value();
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
