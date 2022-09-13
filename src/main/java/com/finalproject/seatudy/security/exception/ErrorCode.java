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

    // TodoList Error
    TODOLIST_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 TODO리스트가 존재하지 않습니다."),
    MISMATCH_SELECT_DATE(HttpStatus.BAD_REQUEST, "카테고리 날짜와 일치하지 않습니다."),
    TODOLIST_FORBIDDEN_POST(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 만들 수 없습니다."),
    TODOLIST_FORBIDDEN_UPDATE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 수정할 수 없습니다."),
    TODOLIST_FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 삭제할 수 없습니다."),
    TODOLIST_FORBIDDEN_COMPLETE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 TODO리스트를 완료할 수 없습니다."),
    DUPLICATE_TODOLIST(HttpStatus.CONFLICT,"동일한 TODO리스트가 존재합니다."),
    EMPTY_TODOLIST(HttpStatus.BAD_REQUEST, "TODO리스트를 입력해주세요.(최소 1자이상)"),


    // TodoCategory Error
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 카테고리가 존재하지 않습니다."),
    CATEGORY_FORBIDDEN_UPDATE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 카테고리를 수정할 수 없습니다."),
    CATEGORY_FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 카테고리를 삭제할 수 없습니다."),
    SELECTDATE_NOT_SAME(HttpStatus.CONFLICT, "수정할 카테고리의 날짜는 다른날짜 입니다."),
    DUPLICATE_CATEGORY(HttpStatus.CONFLICT, "동일한 카테고리가 존재합니다."),
    EMPTY_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리 이름을 입력해주세요(최소 1자이상)"),

    // D-day Error
    TITLE_NOT_EMPTY(HttpStatus.BAD_REQUEST,"타이틀을 입력해야 합니다." ),
    DDAY_NOT_FOUND(HttpStatus.BAD_REQUEST, "디데이가 존재하지 않습니다." ),
    DDAY_FORBIDDEN_UPDATE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 디데이를 수정할 수 없습니다."),
    DDAY_FORBIDDEN_DELETE(HttpStatus.FORBIDDEN, "현재 사용자는 해당 디데이를 삭제할 수 없습니다."),

    // Time_check Error
    CHECKOUT_NOT_TRY(HttpStatus.BAD_REQUEST, "연속으로 체크인을 할 수 없습니다."),
    CHECKIN_NOT_TRY(HttpStatus.BAD_REQUEST,"연속으로 체크아웃을 할 수 없습니다.");
    // Chat Error

    private final HttpStatus httpStatus;
    private final String message;
}
