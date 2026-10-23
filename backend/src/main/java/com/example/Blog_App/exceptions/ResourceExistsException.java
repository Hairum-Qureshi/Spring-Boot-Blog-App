package com.example.Blog_App.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceExistsException extends IllegalArgumentException {

    public ResourceExistsException(boolean vague, String message) {
        super(vague ? "Resource already exists" : message);
    }
}
