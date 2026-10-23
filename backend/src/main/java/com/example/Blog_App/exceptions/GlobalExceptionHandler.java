package com.example.Blog_App.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException exception) {

        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                404,
                "RESOURCE_NOT_FOUND"
        );

        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceExists(ResourceExistsException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                409,
                "RESOURCE_ALREADY_EXISTS"
        );

        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(PasswordsNotMatchingException.class)
    public ResponseEntity<ErrorResponse> handlePasswordsNotMatching(PasswordsNotMatchingException exception) {
        ErrorResponse error = new ErrorResponse(
                exception.getMessage(),
                400,
                "PASSWORDS_NOT_MATCHING"
        );

        return ResponseEntity.status(400).body(error);
    }
}
