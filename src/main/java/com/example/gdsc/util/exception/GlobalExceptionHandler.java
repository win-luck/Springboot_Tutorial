package com.example.gdsc.util.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler는 특정 예외가 발생했을 때, 진행을 중단하고 예외를 처리하는 클래스입니다.
 * Controller를 통해 클라이언트가 요청한 작업을 Service에서 처리하다가 예외가 발생하면, 이를 GlobalExceptionHandler가 가장 먼저 대응합니다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class) // CustomException이 발생하면 가장 먼저 호출됩니다.
    public ResponseEntity<Void> handleUserException(CustomException e) {
        return ResponseEntity.status(e.getResponseCode().getHttpStatus()).build();
    }
}