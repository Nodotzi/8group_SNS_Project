package com.sparta.snsproject.exception;

import com.sparta.snsproject.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.locks.ReentrantLock;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllException(Exception e) {
        return new ResponseEntity<>("서버 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateNickNameException.class)
    public ApiResponse<?> handleCourseNameException(DuplicateNickNameException e) {
        return ApiResponse.createError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ApiResponse<?> handleCourseNameException(DuplicateEmailException e) {
        return ApiResponse.createError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
}

