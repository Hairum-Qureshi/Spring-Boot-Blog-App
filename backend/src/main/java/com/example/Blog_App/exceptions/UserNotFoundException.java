package com.example.Blog_App.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(boolean vague, String message) {
        super(vague ? "User not found" : "User not found with " + message);
    }
}
