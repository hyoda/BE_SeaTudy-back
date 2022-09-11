package com.finalproject.seatudy.security.exception;

import com.finalproject.seatudy.service.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }


    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode){
        return ResponseEntity.status(errorCode.getHttpStatus()).body(makeErrorResponse(errorCode));
    }

    private ResponseDto<?> makeErrorResponse(ErrorCode errorCode){

        return ResponseDto.fail(errorCode.name(),errorCode.getMessage());

    }
}
