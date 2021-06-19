package com.picture.diary.result;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class Result<T> {
    private Results resultCode;
    private String message;

    private T result;
}
