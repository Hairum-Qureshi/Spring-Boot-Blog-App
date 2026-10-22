package com.example.Blog_App.user;

import com.example.Blog_App.user.enums.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String name,

        boolean enabled,

        String profilePicture,

        String biography,

        Instant createdAt,

        Instant updatedAt,

        Provider provider
) {
    // Default provider when null is supplied
    public UserDto {
        if (provider == null) {
            provider = Provider.LOCAL;
        }
    }
}
