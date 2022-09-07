package com.finalproject.seatudy.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "해당 유저가 존재하지 않습니다."),
    //
    USER_DELETED(HttpStatus.BAD_REQUEST, "U002", "삭제된 유저.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public ErrorResponse toErrorResponse() {
        return new ErrorResponse(code, message,httpStatus);
    }
}
