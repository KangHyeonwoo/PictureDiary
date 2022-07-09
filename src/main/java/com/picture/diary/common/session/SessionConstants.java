package com.picture.diary.common.session;

//ref : https://atoz-develop.tistory.com/entry/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8%EC%97%90%EC%84%9C-%EC%BF%A0%ED%82%A4-%EC%84%B8%EC%85%98%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0

/**
 * HttpSession 의 Key 값을 상수화 한 인터페이스
 *
 * 객체 생성을 불가능하게 하기 위해 Interface 로 생성.
 * (abstract class 로 생성해도 같은 효과를 볼 수 있음음
 */
public interface SessionConstants {

    String LOGIN_USER = "loginUser";
}
