package com.example.Blog_App.user.entities;

import com.example.Blog_App.user.entities.enums.Provider;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Builder.Default
    private String profilePicture = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDVHG83UUjZpWbFG01RcvmHsOYq81sD05yTKbnq5tW6A&s";

    @Builder.Default
    private boolean enabled = true;

    private String biography;

    @Builder.Default
    private Instant createdAt = Instant.now(); // time in UTC

    @Builder.Default
    private Instant updatedAt = Instant.now(); // time in UTC

    @Builder.Default
    private Provider provider = Provider.LOCAL;
}
