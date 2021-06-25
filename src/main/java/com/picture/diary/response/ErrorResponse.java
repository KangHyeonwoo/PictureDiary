package com.picture.diary.response;

import lombok.Getter;

@Getter
public class ErrorResponse extends BasicResponse{

	private String message;
	
	public ErrorResponse(String message) {
		this.message = message;
	}
}
