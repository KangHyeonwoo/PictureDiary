package com.picture.diary.extract.exception;

import com.picture.diary.common.exception.BaseExceptionType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PictureExtractExceptionType implements BaseExceptionType {
	NOT_ALLOW_EXTENSION(1001, 400, "허용하지 않는 확장자입니다."),
	FILE_NOT_FOUND(1001, 404, "파일을 찾을 수 없습니다."),
	METADATA_WRITE_FAILED(1001, 500, "파일 속성정보 수정에 실패하였습니다."),
	METADATA_READ_FAILED(1001, 500, "파일 속성정보를 읽는 중 에러가 발생했습니다."),
	ETC_EXCEPTION(1001, 500, "에러가 발생했습니다.");
	
	private int errorCode;
	private int httpStatus;
	private String errorMessage;
	
}
