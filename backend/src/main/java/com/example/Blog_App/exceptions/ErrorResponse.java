package com.example.Blog_App.exceptions;

public record ErrorResponse(String message, int status, String error) {
}
