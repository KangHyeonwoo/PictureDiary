package com.picture.diary.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse extends BasicResponse{

	private String errorCode;
	private int status;
	private String message;
	
	public ErrorResponse(String message) {
		this.message = message;
	}
}
