package com.example.Blog_App.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            CredentialsExpiredException.class,
            DisabledException.class
    })
    public ResponseEntity<ApiError> handleAuthException(Exception e, HttpServletRequest request) {
        var apiError = ApiError.of(HttpStatus.UNPROCESSABLE_CONTENT.value(), "Bad Request", e.getMessage(), request.getRequestURI());
        return ResponseEntity.badRequest().body(apiError);
    }

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
