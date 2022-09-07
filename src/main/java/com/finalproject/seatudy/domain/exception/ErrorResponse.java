package com.finalproject.seatudy.domain.exception;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;

@RequiredArgsConstructor
public class ErrorResponse {

    private final String errorCode;
    private final String errorMessage;

}