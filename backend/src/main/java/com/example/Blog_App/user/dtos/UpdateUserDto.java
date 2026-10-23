package com.example.Blog_App.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @Size(max = 500, message = "Biography can only have a max length of 500 characters")
        String biography) {
    public UpdateUserDto {
        if (biography == null || biography.isEmpty()) {
            biography = "This user has not set a biography yet.";
        }
    }
}
