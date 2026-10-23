package com.example.Blog_App.user;

import com.example.Blog_App.user.enums.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
        UUID id,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Name is required")
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
