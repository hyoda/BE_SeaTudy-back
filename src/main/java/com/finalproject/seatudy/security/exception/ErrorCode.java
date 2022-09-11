package com.finalproject.seatudy.security.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    // Member Error
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 유저 정보를 찾을 수 없습니다."),


    // Login & Register Error
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATE_REGISTER(HttpStatus.CONFLICT, "이미 다른 소셜로그인으로 가입한 회원입니다."),
    CURRENT_NICKNAME(HttpStatus.CONFLICT, "현재와 같은 닉네임입니다."),
    EMPTY_NICKNAME(HttpStatus.BAD_REQUEST, "닉네임을 입력해주세요.(최소 1자이상)"),


    // TodoList Error
    TODOLIST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 TODO리스트가 존재하지 않습니다."),
    MISMATCH_SELECT_DATE(HttpStatus.BAD_REQUEST, "카테고리 날짜와 일치하지 않습니다."),
    TODOLIST_FORBIDDEN_GET(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 조회할 수 없습니다."),
    TODOLIST_FORBIDDEN_POST(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 만들 수 없습니다."),
    TODOLIST_FORBIDDEN_UPDATE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 수정할 수 없습니다."),
    TODOLIST_FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 삭제할 수 없습니다."),
    TODOLIST_FORBIDDEN_COMPLETE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 완료할 수 없습니다."),

    // TodoCategory Error
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 카테고리가 존재하지 않습니다."),
    CATEGORY_FORBIDDEN_GET(HttpStatus.FORBIDDEN, "현재 사용자는 해당 카테고리를 조회할 수 없습니다."),
    CATEGORY_FORBIDDEN_POST(HttpStatus.FORBIDDEN, "현재 사용자는 해당 카테고리를 만들 수 없습니다."),
    CATEGORY_FORBIDDEN_UPDATE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 카테고리를 수정할 수 없습니다."),
    CATEGORY_FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 카테고리를 삭제할 수 없습니다.");

    // D-day Error


    // Time_check Error

    // Chat Error

    private final HttpStatus httpStatus;
    private final String message;
}
