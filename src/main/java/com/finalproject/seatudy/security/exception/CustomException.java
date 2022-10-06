package com.finalproject.seatudy.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
}
