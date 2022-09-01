package com.finalproject.seatudy.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // Token Error
    INVALID_AUTH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    AUTH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "토큰이 존재하지 않습니다."),

    // Member Error
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 유저 정보를 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;

}
