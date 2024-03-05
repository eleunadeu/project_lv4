package com.sparta.lecture.global.handler;

import com.sparta.lecture.global.dto.ResponseDto;
import com.sparta.lecture.global.handler.exception.CustomApiException;
import com.sparta.lecture.global.handler.exception.CustomValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<Object> handleCustomApiException(CustomApiException e) {
        return ResponseEntity.badRequest().body(new ResponseDto<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<Object> handlerCustomValidationApiException(CustomValidationException e) {
        return ResponseEntity.badRequest().body(new ResponseDto<>(false, e.getMessage(), e.getErrorMap()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> processValidationError(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(new ResponseDto<>(false, e.getMessage(), null));
    }
}
