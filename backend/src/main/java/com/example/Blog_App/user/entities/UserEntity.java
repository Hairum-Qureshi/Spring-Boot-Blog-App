package com.example.Blog_App.user.entities;

import com.example.Blog_App.user.entities.enums.Provider;
import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private String profilePicture = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDVHG83UUjZpWbFG01RcvmHsOYq81sD05yTKbnq5tW6A&s";

    @Builder.Default
    private boolean enabled = true;

    @Column(nullable = false, length = 500)
    private String biography = "This user has not set a biography yet.";

    @Builder.Default
    private Instant createdAt = Instant.now(); // time in UTC

    @Builder.Default
    private Instant updatedAt = Instant.now(); // time in UTC

    @Builder.Default
    private Provider provider = Provider.LOCAL;
}
