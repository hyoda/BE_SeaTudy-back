package com.finalproject.seatudy.advice;

import com.finalproject.seatudy.domain.exception.ErrorResponse;
import com.finalproject.seatudy.domain.exception.UserErrorCode;
import com.finalproject.seatudy.domain.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerExceptionHandlerAdvice {

    @ExceptionHandler(UserException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUserException(UserException userException) {
        UserErrorCode userErrorCode = userException.getUserErrorCode();
        return new ResponseEntity<>(userErrorCode.toErrorResponse(), userErrorCode.getHttpStatus());
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
//        return new ResponseEntity<>(
//                new ErrorResponse(
//                        "INTERNAL_SERVER_ERROR",
//                        exception.getMessage()
//                ),
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }
}
