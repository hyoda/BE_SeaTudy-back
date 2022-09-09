package com.finalproject.seatudy.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // Common Error
    INVALID_AUTH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    AUTH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),

    // Member Error
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 유저 정보를 찾을 수 없습니다."),



    // Login & Register Error
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다.");



    // TodoList Error



    // D-day Error


    // Time_check Error

    // Chat Error

    private final HttpStatus httpStatus;
    private final String message;
}
