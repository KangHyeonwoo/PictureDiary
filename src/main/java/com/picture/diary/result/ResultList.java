package com.picture.diary.result;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ResultList<T> {
    private Results resultCode;
    private String message;

    private List<T> resultList;

    @Builder
    public ResultList(Results resultCode, String message, List<T> resultList) {
        this.resultCode = resultCode;
        this.message = message;
        this.resultList = resultList;
    }
}
