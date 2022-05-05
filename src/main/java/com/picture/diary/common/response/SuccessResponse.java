package com.picture.diary.common.response;

import java.util.List;

import lombok.Getter;

@Getter
public class SuccessResponse<T> extends BasicResponse {
	
	private T data;
	private int count;
	
	public SuccessResponse() {
		this.count = 1;
	}
	
	public SuccessResponse(T data) {
		this.data = data;
		if(data instanceof List) {
			this.count = ((List<?>) data).size();
		} else {
			this.count = 1;
		}
	}
}
