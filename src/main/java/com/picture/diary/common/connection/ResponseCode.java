package com.picture.diary.common.connection;

public enum ResponseCode {
    //응답 성공. 정상적으로 데이터가 리턴된 경우
    SUCCESS,

    //응답은 성공하였으나 잘못된 요청 혹은, 필수 파라미터 누락으로 인해 응답 결과가 실패인 경우
    FAIL,

    //요청 자체를 실패한 경우
    ERROR;
}
