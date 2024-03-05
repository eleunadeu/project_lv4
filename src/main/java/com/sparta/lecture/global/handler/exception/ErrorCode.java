//package com.sparta.lecture.global.handler.exception;
//
//import lombok.Getter;
//
//@Getter
//public class ErrorCode {
//
//    //로그인
//    NOT_FOUND_ADMIN_ID("찾을 수 없는 관리자입니다."),
//    NOT_FOUND_ADMIN_PW("찾을 수 없는 관리자 비밀번호 입니다."),
//    NOT_FOUND_USER_ID("찾을 수 없는 유저입니다."),
//    NOT_FOUND_USER_PW("찾을 수 없는 유저 비밀번호 입니다."),
//
//    //공통
//    TOKEN_NOT_VALID("토큰이 일치하지 않습니다.")
//    BAD_REQUEST_USER_AUTHENTICATION("인증되지 않은 사용자 입니다."),
//
//    //강사
//    NOT_FOUND_TUTOR_ID("찾을 수 없는 강사 정보입니다."),
//    //강의
//    NOT_FOUND_CATEGORY_ID("찾을 수 없는 강의 종류 입니다."),//카테고리
//    NOT_FOUND_LECTURE_INFORMATION("찾을 수 없는 강의 정보입니다."),//강의 정보조회 시
//
//    //회원가입
//    ALREADY_REGISTERED_PHONE("이미 가입된 휴대폰 번호입니다."),
//    ALREADY_REGISTERED_IDENTIFICATION("이미 가입된 주민번호입니다."),
//
//    ;
//
//    private final String message;
//
//    ErrorCode(String message) {
//        this.message = message;
//    }
//}
