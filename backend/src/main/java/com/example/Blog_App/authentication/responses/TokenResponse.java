package com.example.Blog_App.authentication.responses;

import com.example.Blog_App.user.dtos.UserDto;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String tokenType,
        UserDto user
) {
    public static TokenResponse of(String accessToken, String refreshToken, long expiresIn, UserDto user) {
        return new TokenResponse(accessToken, refreshToken, expiresIn, "Bearer", user);
    }
}
