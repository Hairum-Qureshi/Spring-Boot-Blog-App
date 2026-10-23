package com.example.Blog_App.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException exception) {

        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                404,
                "USER_NOT_FOUND"
        );

        return ResponseEntity.status(404).body(error);
    }
}
