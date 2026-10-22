package com.example.Blog_App.user;

import com.example.Blog_App.user.enums.Provider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private UUID id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    private boolean enabled;

    private String profilePicture;

    private String biography;

    private Instant createdAt;

    private Instant updatedAt;

    private Provider provider = Provider.LOCAL;
}
